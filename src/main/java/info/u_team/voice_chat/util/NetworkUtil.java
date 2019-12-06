package info.u_team.voice_chat.util;

import java.net.*;

import info.u_team.voice_chat.config.CommonConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.*;

public class NetworkUtil {
	
	public static InetSocketAddress findServerBindAddress() {
		final MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
		final int port = CommonConfig.getInstance().portValue.get();
		if (server.getServerHostname() != null && !server.getServerHostname().isEmpty()) {
			return new InetSocketAddress(server.getServerHostname(), port);
		}
		return new InetSocketAddress(port);
	}
	
	public static InetSocketAddress findServerInetAddress(int port) {
		final Minecraft client = LogicalSidedProvider.INSTANCE.get(LogicalSide.CLIENT);
		if (client.getCurrentServerData() != null) {
			return new InetSocketAddress(ServerAddress.fromString(client.getCurrentServerData().serverIP).getIP(), port);
		}
		return new InetSocketAddress("localhost", port);
	}
	
}
