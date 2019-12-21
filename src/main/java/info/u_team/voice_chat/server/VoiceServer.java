package info.u_team.voice_chat.server;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import info.u_team.voice_chat.packet.PacketRegistry;
import info.u_team.voice_chat.packet.PacketRegistry.Context.Sender;
import info.u_team.voice_chat.server.VerifiedPlayerManager.PlayerData;
import info.u_team.voice_chat.util.NetworkUtil;
import net.minecraft.entity.player.ServerPlayerEntity;

public class VoiceServer {
	
	private final DatagramSocket socket;
	
	private final Future<?> task;
	
	public VoiceServer(ExecutorService service) throws SocketException {
		socket = new DatagramSocket(NetworkUtil.findServerBindAddress());
		task = service.submit(() -> {
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
					
					final ServerPlayerEntity player = PlayerSecretManager.getPlayerBySecret(secret);
					
					if (player == null) { // Ignore packets that do not have a valid player as sender
						return;
					}
					
					final byte[] data = new byte[buffer.remaining()];
					buffer.get(data);
					
					final Object message = PacketRegistry.decode(data, data.length);
					if (message != null) {
						PacketRegistry.handle(message, Sender.PLAYER, (InetSocketAddress) packet.getSocketAddress(), player);
					}
				} catch (IOException ex) {
					if (!socket.isClosed()) {
						ex.printStackTrace();
					}
				}
			}
		});
	}
	
	public <MSG> void sendPlayer(ServerPlayerEntity player, MSG message) {
		sendIntern(Arrays.asList(VerifiedPlayerManager.getPlayerData(player)), message);
	}
	
	public <MSG> void sendAll(MSG message) {
		sendIntern(VerifiedPlayerManager.getMap().values(), message);
	}
	
	public <MSG> void sendAllExcept(MSG message, ServerPlayerEntity except) {
		sendIntern(VerifiedPlayerManager.getMap().entrySet().stream().filter(entry -> !entry.getKey().equals(except.getUniqueID())).map(Entry::getValue).collect(Collectors.toList()), message);
	}
	
	private <MSG> void sendIntern(Collection<PlayerData> players, MSG message) {
		final byte[] data = PacketRegistry.encode(message);
		if (data == null) {
			return;
		}
		players.stream().forEach(playerData -> {
			try {
				socket.send(new DatagramPacket(data, data.length, playerData.getAddress()));
			} catch (IOException ex) {
				if (!socket.isClosed()) {
					ex.printStackTrace();
				}
			}
		});
	}
	
	public void close() {
		socket.close();
		task.cancel(true);
	}
	
}
