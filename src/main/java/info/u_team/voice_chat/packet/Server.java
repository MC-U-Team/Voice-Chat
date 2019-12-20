package info.u_team.voice_chat.packet;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;

import info.u_team.voice_chat.packet.PacketRegistry.Context.Sender;
import info.u_team.voice_chat.server.*;
import info.u_team.voice_chat.util.NetworkUtil;
import net.minecraft.entity.player.ServerPlayerEntity;

public class Server {
	
	private final DatagramSocket socket;
	
	private final Future<?> task;
	
	public Server() throws SocketException {
		socket = new DatagramSocket(NetworkUtil.findServerBindAddress());
		task = PacketRegistry.EXECUTOR.submit(() -> {
			while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
				try {
					final DatagramPacket packet = new DatagramPacket(new byte[PacketRegistry.MAX_PACKET_SIZE + 8], PacketRegistry.MAX_PACKET_SIZE + 8);
					socket.receive(packet);
					
					if (packet.getLength() < 9) { // Ignore too small packets (8 bytes secret + 1 the packet id)
						return;
					}
					
					final ByteBuffer buffer = ByteBuffer.wrap(packet.getData(), 0, packet.getLength());
					
					final byte[] secret = new byte[8];
					buffer.get(secret);
					
					final ServerPlayerEntity player = PlayerSecretList.getPlayerBySecret(secret);
					
					if (player == null) { // Ignore packets that do not have a valid player as sender
						return;
					}
					
					final byte[] data = new byte[buffer.remaining()];
					buffer.get(data);
					
					final Object message = PacketRegistry.decode(data, data.length);
					if (message != null) {
						PacketRegistry.handle(message, Sender.PLAYER, player);
					}
				} catch (IOException ex) {
					if (!socket.isClosed()) {
						ex.printStackTrace();
					}
				}
			}
		});
	}
	
	public <MSG> void send(ServerPlayerEntity player, MSG message) {
		final byte[] data = PacketRegistry.encode(message);
		if (data == null) {
			return;
		}
		try {
			socket.send(new DatagramPacket(data, data.length, VerifiedPlayerDataList.getPlayerData(player).getAddress()));
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
