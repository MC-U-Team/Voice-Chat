package info.u_team.voice_chat.gui;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import info.u_team.voice_chat.VoiceChatMod;
import info.u_team.voice_chat.client.TalkingList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = VoiceChatMod.MODID, value = Dist.CLIENT, bus = Bus.FORGE)
public class GuiOverlayHandler {
	
	@SubscribeEvent
	public static void render(RenderGameOverlayEvent.Post event) {
		final Minecraft minecraft = Minecraft.getInstance();
		if (event.getType() != ElementType.ALL || minecraft.gameSettings.showDebugInfo) {
			return;
		}
		Minecraft.getInstance().fontRenderer.drawString("Speakers", 10, 10, 0xFFFFFF);
		AtomicInteger counter = new AtomicInteger();
		TalkingList.getTalkers().stream().map(GuiOverlayHandler::getName).forEach(name -> {
			Minecraft.getInstance().fontRenderer.drawString(name, 10, counter.getAndIncrement() * 15 + 30, 0xFFFFFF);
		});
		
	}
	
	private static String getName(UUID uuid) {
		final ClientPlayNetHandler connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			final NetworkPlayerInfo info = connection.getPlayerInfo(uuid);
			if (info != null) {
				return info.getGameProfile().getName();
			}
		}
		return uuid.toString();
	}
	
}
