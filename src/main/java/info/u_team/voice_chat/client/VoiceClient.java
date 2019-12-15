package info.u_team.voice_chat.client;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;

import info.u_team.voice_chat.audio_client.util.*;
import info.u_team.voice_chat.util.*;
import net.minecraft.client.Minecraft;

public class VoiceClient {
	
	protected final VoiceRecorder recorder;
	protected final VoicePlayer player;
	
	protected final byte[] secret;
	protected final DatagramSocket socket;
	
	protected final InetSocketAddress serverAddress;
	
	protected final Thread receiveThread;
	protected final Thread sendThread;
	
	protected boolean handshakeDone;
	
	public VoiceClient(int port, byte[] secret) throws SocketException {
		this.secret = Arrays.copyOf(secret, secret.length);
		recorder = new VoiceRecorder();
		player = new VoicePlayer();
		socket = new DatagramSocket();
		serverAddress = NetworkUtil.findServerInetAddress(port);
		receiveThread = new Thread(() -> receiveTask(), "Voice Client Receive");
		sendThread = new Thread(() -> sendTask(), "Voice Client Send");
		receiveThread.start();
		sendThread.start();
	}
	
	public void close() {
		socket.close();
		recorder.close();
		player.close();
		receiveThread.interrupt();
		sendThread.interrupt();
		try {
			// Wait for both threads to be closed
			receiveThread.join();
			sendThread.join();
		} catch (InterruptedException ex) {
			// Should not happen. Who interrupts the main thread??
		}
	}
	
	private void receiveTask() {
		while (!receiveThread.isInterrupted() && !socket.isClosed()) {
			try {
				receivePacket();
			} catch (IOException ex) {
				if (!socket.isClosed()) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	private void sendTask() {
		try {
			// If handshake has not been done yet, send the packet every 500 ms
			while (!handshakeDone && !socket.isClosed()) {
				try {
					sendHandshakePacket();
					synchronized (this) {
						Thread.sleep(500);
					}
				} catch (IOException ex) {
					if (!socket.isClosed()) {
						ex.printStackTrace();
					}
				}
			}
			while (!sendThread.isInterrupted() && !socket.isClosed()) {
				try {
					sendPacket();
				} catch (IOException ex) {
					if (!socket.isClosed()) {
						ex.printStackTrace();
					}
				}
			}
		} catch (InterruptedException ex) {
		}
	}
	
	public void setHandshakeDone() {
		handshakeDone = true;
	}
	
	protected void sendPacket() throws IOException, InterruptedException {
		if (recorder.canSend()) {
			final byte[] opusPacket = recorder.getBytes();
			if (opusPacket.length > 1) {
				sendOpusPacket(PacketType.VOICE, opusPacket);
			}
		} else {
			synchronized (this) {
				Thread.sleep(50);
			}
		}
	}
	
	protected void sendHandshakePacket() throws IOException {
		final ByteBuffer buffer = ByteBuffer.allocate(9);
		buffer.put(PacketType.HANDSHAKE.getID());
		buffer.put(secret);
		
		socket.send(new DatagramPacket(buffer.array(), buffer.capacity(), serverAddress));
	}
	
	protected void sendOpusPacket(PacketType type, byte[] opusPacket) throws IOException {
		final ByteBuffer buffer = ByteBuffer.allocate(9 + opusPacket.length);
		buffer.put(type.getID());
		buffer.put(secret);
		buffer.put(opusPacket);
		TalkingList.addOrUpdate(Minecraft.getInstance().player.getUniqueID()); // Add the client to the talker list if he is talking
		socket.send(new DatagramPacket(buffer.array(), buffer.capacity(), serverAddress));
	}
	
	protected void receivePacket() throws IOException {
		final DatagramPacket packet = new DatagramPacket(new byte[800], 800);
		socket.receive(packet);
		
		final ByteBuffer buffer = ByteBuffer.wrap(packet.getData(), 0, packet.getLength());
		final short id = buffer.getShort();
		
		final UUID uuid = PlayerIDList.getPlayerByID(id);
		if (uuid == null) {
			System.out.println("Unknown uuid. That should not happen");
			return;
		}
		
		final byte[] data = new byte[buffer.remaining()];
		buffer.get(data);
		
		handleVoicePacket(uuid, data);
	}
	
	protected void handleVoicePacket(UUID uuid, byte[] packet) {
		if (player.canPlay()) {
			player.play(packet);
		}
		TalkingList.addOrUpdate(uuid);
	}
	
}
