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

public class VoiceServer implements Runnable {
	
	public final DatagramSocket socket;
	
	public VoiceServer() throws SocketException {
		SocketAddress address = NetworkUtil.findServerBindAddress();
		socket = new DatagramSocket(address);
		System.out.println("----------------------------------------------<yyyyyyyYY " + address);
	}
	
	public void close() {
		socket.close();
	}
	
	@Override
	public void run() {
		while (!socket.isClosed()) {
			try {
				System.out.println("LOL YES -----------------------------------------------------------");
				receivePacket();
				
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		System.out.println("WTF SHOULD NOT HAPPEN LOLLL LL LXDODODODODOODODODODO");
	}
	
	private void receivePacket() throws IOException {
		final DatagramPacket packet = new DatagramPacket(new byte[1000], 1000);
		socket.receive(packet);
		
		final byte[] data = packet.getData();
		final byte type = data[0];
		final ServerPlayerEntity player = PlayerSecretList.getPlayerBySecret(Arrays.copyOfRange(data, 1, 9));
		
		System.out.println("RECEIVED PACKET YXSADASDHJASDJKL HASJDKLHALSJKDhalsKHDJ");
		
		// Ignore packet if the secret cannot be matches to a player
		if (player == null) {
			return;
		}
		
		if (type == 0) {
			handleHandshakePacket(player, packet.getAddress(), packet.getPort());
		}
		
	}
	
	private void handleHandshakePacket(ServerPlayerEntity player, InetAddress address, int port) {
		// DO NOT DO ANYTHING IF ALREADY HERE:
		VerifiedPlayerDataList.addPlayer(player, new PlayerData(address, port));
		VoiceChatNetworks.NETWORK.send(PacketDistributor.PLAYER.with(() -> player), new ReadyMessage());
	}
	
}
