package info.u_team.voice_chat.packet;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.Future;

import info.u_team.voice_chat.packet.PacketRegistry.Context.Sender;
import info.u_team.voice_chat.util.NetworkUtil;

public class Client {
	
	private final DatagramSocket socket;
	
	private final InetSocketAddress serverAddress;
	
	private final byte[] secret;
	
	private final Future<?> task;
	
	public Client(int port, byte[] secret) throws SocketException {
		this.secret = Arrays.copyOf(secret, secret.length);
		socket = new DatagramSocket();
		serverAddress = NetworkUtil.findServerInetAddress(port);
		task = PacketRegistry.EXECUTOR.submit(() -> {
			while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
				try {
					final DatagramPacket packet = new DatagramPacket(new byte[PacketRegistry.MAX_PACKET_SIZE + 2], PacketRegistry.MAX_PACKET_SIZE + 2);
					socket.receive(packet);
					
					if (packet.getLength() < 1) { // Ignore too small packets (1 byte the packet id)
						return;
					}
					
					final Object message = PacketRegistry.decode(packet.getData(), packet.getLength());
					if (message != null) {
						PacketRegistry.handle(message, Sender.SERVER);
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
		task.cancel(true);
	}
}
