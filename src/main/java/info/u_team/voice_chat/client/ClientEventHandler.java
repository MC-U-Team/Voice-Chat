package info.u_team.voice_chat.client;

import info.u_team.voice_chat.VoiceChatMod;
import info.u_team.voice_chat.audio.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = VoiceChatMod.MODID, value = Dist.CLIENT, bus = Bus.FORGE)
public class ClientEventHandler {
	
	@SubscribeEvent
	public static void logout(LoggedOutEvent event) {
		if (MicroManager.isRunning()) {
			MicroManager.stop();
		}
		if (SpeakerManager.isRunning()) {
			SpeakerManager.stop();
		}
		if (VoiceClientManager.isRunning()) {
			VoiceClientManager.stop();
		}
		PlayerIDManager.clear();
		TalkingManager.clear();
	}
}
