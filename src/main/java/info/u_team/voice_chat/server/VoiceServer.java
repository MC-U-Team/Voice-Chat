package info.u_team.voice_chat.server;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

import info.u_team.voice_chat.init.VoiceChatNetworks;
import info.u_team.voice_chat.message.ReadyMessage;
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
		final DatagramPacket packet = new DatagramPacket(new byte[1000], 1000);
		socket.receive(packet);
		
		final byte[] data = packet.getData();
		final byte type = data[0];
		final ServerPlayerEntity player = PlayerSecretList.getPlayerBySecret(Arrays.copyOfRange(data, 1, 9));
		
		// Ignore packet if the secret cannot be matches to a player
		if (player == null) {
			return;
		}
		
		if (type == 0) {
			handleHandshakePacket(player, packet.getAddress(), packet.getPort());
		}
		
	}
	
	private void handleHandshakePacket(ServerPlayerEntity player, InetAddress address, int port) {
		if (!VerifiedPlayerDataList.hasPlayerData(player)) {
			VerifiedPlayerDataList.addPlayer(player, new PlayerData(address, port));
			VoiceChatNetworks.NETWORK.send(PacketDistributor.PLAYER.with(() -> player), new ReadyMessage());
		}
	}
	
}
