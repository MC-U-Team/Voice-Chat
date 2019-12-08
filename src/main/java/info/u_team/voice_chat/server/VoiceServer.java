package info.u_team.voice_chat.server;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.Map.Entry;

import info.u_team.voice_chat.init.VoiceChatNetworks;
import info.u_team.voice_chat.message.*;
import info.u_team.voice_chat.server.VerifiedPlayerDataList.PlayerData;
import info.u_team.voice_chat.util.NetworkUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

public class VoiceServer {
	
	private final DatagramSocket socket;
	
	private final Thread thread;
	
	public VoiceServer() throws SocketException {
		socket = new DatagramSocket(NetworkUtil.findServerBindAddress());
		thread = new Thread(() -> serverTask(), "Voice Server");
		thread.start();
	}
	
	public void close() {
		socket.close();
		thread.interrupt();
		try {
			// Wait for the thread to be closed
			thread.join();
		} catch (InterruptedException ex) {
			// Should not happen. Who interrupts the main thread??
		}
	}
	
	public void serverTask() {
		while (!thread.isInterrupted() && !socket.isClosed()) {
			try {
				receivePacket();
			} catch (IOException ex) {
				if (!socket.isClosed()) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	private void receivePacket() throws IOException {
		final DatagramPacket packet = new DatagramPacket(new byte[1500], 1500);
		socket.receive(packet);
		
		final byte[] data = packet.getData();
		final byte type = data[0];
		final ServerPlayerEntity player = PlayerSecretList.getPlayerBySecret(Arrays.copyOfRange(data, 1, 9));
		final byte[] transmittedData = Arrays.copyOfRange(data, 9, packet.getLength() /** + 1 ?? */
		);
		
		// Ignore packet if the secret cannot be matches to a player
		if (player == null) {
			return;
		}
		
		if (type == 0) {
			handleHandshakePacket(player, (InetSocketAddress) packet.getSocketAddress());
		} else if (type == 1) {
			handleVoicePacket(player, transmittedData);
		}
		
	}
	
	private void handleHandshakePacket(ServerPlayerEntity player, InetSocketAddress address) {
		if (!VerifiedPlayerDataList.hasPlayerData(player)) {
			final PlayerData data = new PlayerData(address);
			VerifiedPlayerDataList.addPlayer(player, data);
			VoiceChatNetworks.NETWORK.send(PacketDistributor.PLAYER.with(() -> player), new ReadyMessage());
			VoiceChatNetworks.NETWORK.send(PacketDistributor.ALL.noArg(), new PlayerIDMessage(false, player.getUniqueID(), data.getId()));
		}
	}
	
	private void handleVoicePacket(ServerPlayerEntity player, byte[] transmittedData) throws IOException {
		// Build packet
		// TODO Add player id so the client can display which client send that voice packet
		
		// No logic, just send it to all players currently
		
		for (Entry<UUID, PlayerData> entry : VerifiedPlayerDataList.getMap().entrySet()) {
			// TODO Should check if its not the sender, for testing we will send it to the sender too
			try {
				socket.send(new DatagramPacket(transmittedData, transmittedData.length, entry.getValue().getAddress()));
			} catch (IOException ex) {
				// We don't want to break if to one "client" we cannot send the data. Just log it for now
				ex.printStackTrace();
			}
		}
	}
	
}
