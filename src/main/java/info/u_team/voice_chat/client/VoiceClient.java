package info.u_team.voice_chat.client;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;

import info.u_team.voice_chat.util.NetworkUtil;

public class VoiceClient {
	
	private final byte[] secret;
	private final DatagramSocket socket;
	
	private final InetSocketAddress serverAddress;
	
	private final Thread receiveThread;
	private final Thread sendThread;
	
	private final VoiceRecorder recorder;
	private final VoicePlayer player;
	
	private boolean handshakeDone;
	
	public VoiceClient(int port, byte[] secret) throws SocketException {
		this.secret = Arrays.copyOf(secret, secret.length);
		socket = new DatagramSocket();
		serverAddress = NetworkUtil.findServerInetAddress(port);
		receiveThread = new Thread(() -> receiveTask(), "Voice Client Receive");
		sendThread = new Thread(() -> sendTask(), "Voice Client Send");
		receiveThread.start();
		sendThread.start();
		recorder = new VoiceRecorder();
		player = new VoicePlayer();
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
	
	private void sendPacket() throws IOException, InterruptedException {
		if (recorder.canSend()) {
			final byte[] opusPacket = recorder.getBytes();
			if (opusPacket.length > 1) {
				sendVoicePacket(opusPacket);
			}
		} else {
			synchronized (this) {
				Thread.sleep(50);
			}
		}
	}
	
	private void sendHandshakePacket() throws IOException {
		final ByteBuffer buffer = ByteBuffer.allocate(9);
		buffer.put((byte) 0);
		buffer.put(secret);
		
		socket.send(new DatagramPacket(buffer.array(), buffer.capacity(), serverAddress));
	}
	
	private void sendVoicePacket(byte[] opusPacket) throws IOException {
		final ByteBuffer buffer = ByteBuffer.allocate(9 + opusPacket.length);
		buffer.put((byte) 1);
		buffer.put(secret);
		buffer.put(opusPacket);
		
		socket.send(new DatagramPacket(buffer.array(), buffer.capacity(), serverAddress));
	}
	
	private void receivePacket() throws IOException {
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
	
	private void handleVoicePacket(UUID uuid, byte[] packet) {
		if (player.canPlay()) {
			player.play(packet);
		}
		// System.out.println(Minecraft.getInstance().world.getPlayerByUuid(uuid));
	}
	
}
