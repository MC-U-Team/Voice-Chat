package info.u_team.voice_chat.server;

import java.net.SocketException;

import info.u_team.voice_chat.VoiceChatMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.server.*;

@EventBusSubscriber(modid = VoiceChatMod.MODID, bus = Bus.FORGE)
public class ServerServerEventHandler {
	
	private static VoiceServer server;
	
	@SubscribeEvent
	public static void start(FMLServerStartingEvent event) {
		try {
			server = new VoiceServer();
			new Thread(server, "Voice Server").start();
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
	}
	
	@SubscribeEvent
	public static void stop(FMLServerStoppingEvent event) {
		if (server != null) {
			server.close();
			server = null;
		}
	}
	
}
