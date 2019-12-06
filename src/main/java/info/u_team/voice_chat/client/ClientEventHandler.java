package info.u_team.voice_chat.client;

import info.u_team.voice_chat.VoiceChatMod;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = VoiceChatMod.MODID, bus = Bus.FORGE)
public class ClientEventHandler {
	
	@SubscribeEvent
	public static void logout(LoggedOutEvent event) {
		if (VoiceClientManager.isRunning()) {
			VoiceClientManager.stop();
		}
	}
	
}
