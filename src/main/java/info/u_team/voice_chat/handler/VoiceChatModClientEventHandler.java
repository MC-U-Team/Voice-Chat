package info.u_team.voice_chat.handler;

import info.u_team.voice_chat.VoiceChatMod;
import info.u_team.voice_chat.render.RenderOverlayVoiceChat;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = VoiceChatMod.MODID, value = Dist.CLIENT, bus = Bus.FORGE)
public class VoiceChatModClientEventHandler {
	
	@SubscribeEvent
	public static void render(RenderGameOverlayEvent.Post event) {
		final Minecraft minecraft = Minecraft.getInstance();
		if (event.getType() != ElementType.ALL || minecraft.gameSettings.showDebugInfo) {
			return;
		}
		RenderOverlayVoiceChat.draw();
	}
	
}
