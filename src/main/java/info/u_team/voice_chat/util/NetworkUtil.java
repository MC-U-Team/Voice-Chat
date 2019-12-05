package info.u_team.voice_chat.util;

import java.net.InetSocketAddress;

import info.u_team.voice_chat.config.CommonConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.*;

public class NetworkUtil {
	
	public static InetSocketAddress findAddress() {
		final MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
		final int port = CommonConfig.getInstance().portValue.get();
		if (!server.getServerHostname().isEmpty()) {
			return new InetSocketAddress(server.getServerHostname(), port);
		}
		return new InetSocketAddress(port);
	}
	
}
