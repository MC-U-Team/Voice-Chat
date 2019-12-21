package info.u_team.voice_chat.client;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.*;

import info.u_team.voice_chat.packet.PacketRegistry;
import info.u_team.voice_chat.packet.PacketRegistry.Context.Sender;
import info.u_team.voice_chat.packet.message.HandshakePacket;
import info.u_team.voice_chat.util.NetworkUtil;

public class VoiceClient {
	
	private final DatagramSocket socket;
	
	private final InetSocketAddress serverAddress;
	
	private final byte[] secret;
	
	private volatile boolean handshakeMode;
	
	private final Future<?> handshakeTask;
	private final Future<?> task;
	
	public VoiceClient(ExecutorService service, int port, byte[] secret) throws SocketException {
		this.secret = Arrays.copyOf(secret, secret.length);
		socket = new DatagramSocket();
		serverAddress = NetworkUtil.findServerInetAddress(port);
		handshakeMode = true;
		
		handshakeTask = service.submit(() -> {
			while (!Thread.currentThread().isInterrupted() && handshakeMode) {
				try {
					System.out.println("SEND HANDSHAKE PACKET");
					sendIntern(new HandshakePacket());
					Thread.sleep(500);
				} catch (InterruptedException ex) {
					System.out.println("EXIT INTER");
					return; // Can happen so we just exist this task
				}
			}
			System.out.println("EXIT LOOPÜ ENDED");
		});
		task = service.submit(() -> {
			while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
				try {
					final DatagramPacket packet = new DatagramPacket(new byte[PacketRegistry.MAX_PACKET_SIZE + 2], PacketRegistry.MAX_PACKET_SIZE + 2);
					socket.receive(packet);
					
					if (packet.getLength() < 1) { // Ignore too small packets (1 byte the packet id)
						return;
					}
					
					final Object message = PacketRegistry.decode(packet.getData(), packet.getLength());
					if (message != null) {
						PacketRegistry.handle(message, Sender.SERVER, (InetSocketAddress) packet.getSocketAddress());
					}
				} catch (IOException ex) {
					if (!socket.isClosed()) {
						ex.printStackTrace();
					}
				}
			}
		});
	}
	
	public <MSG> void send(MSG message) {
		if (handshakeTask.isDone()) {
			sendIntern(message);
		}
	}
	
	private <MSG> void sendIntern(MSG message) {
		final byte[] data = PacketRegistry.encode(message);
		
		final ByteBuffer buffer = ByteBuffer.allocate(8 + data.length);
		buffer.put(secret);
		buffer.put(data);
		
		try {
			socket.send(new DatagramPacket(buffer.array(), buffer.capacity(), serverAddress));
		} catch (IOException ex) {
			if (!socket.isClosed()) {
				ex.printStackTrace();
			}
		}
	}
	
	public void close() {
		socket.close();
		setHandshakeDone();
		task.cancel(true);
	}
	
	public void setHandshakeDone() {
		handshakeTask.cancel(true);
		handshakeMode = false;
		System.out.println("Handshake done ------------------------------------------ >>");
	}
}
