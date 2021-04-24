package info.u_team.voice_chat.client;

import info.u_team.voice_chat.VoiceChatMod;
import info.u_team.voice_chat.api.IIntegration;
import info.u_team.voice_chat.audio.MicroManager;
import info.u_team.voice_chat.audio.SpeakerManager;
import info.u_team.voice_chat.init.VoiceChatIntegrations;
import info.u_team.voice_chat.init.VoiceChatKeys;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggedOutEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
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
		if (TalkingManager.isRunning()) {
			TalkingManager.stop();
		}
		VoiceChatIntegrations.INTEGRATIONS.forEach(IIntegration::stop);
		PlayerIDManager.clear();
	}
	
	@SubscribeEvent
	public static void keyPress(ClientTickEvent event) {
		if (event.phase == Phase.START) {
			if (VoiceChatKeys.PUSH_TALK.isKeyDown()) {
				if (MicroManager.isRunning() && !MicroManager.getHandler().isSending()) {
					MicroManager.getHandler().start();
				}
			} else {
				if (MicroManager.isRunning() && MicroManager.getHandler().isSending()) {
					MicroManager.getHandler().stop();
				}
			}
		}
	}
}
