package info.u_team.voice_chat.handler;

import info.u_team.u_team_core.gui.elements.ScalableButton;
import info.u_team.voice_chat.VoiceChatMod;
import info.u_team.voice_chat.gui.VoiceChatSettingsGui;
import info.u_team.voice_chat.musicplayer_integration.util.MusicPlayerIntegrationUtil;
import info.u_team.voice_chat.render.RenderOverlayVoiceChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.*;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = VoiceChatMod.MODID, value = Dist.CLIENT, bus = Bus.FORGE)
public class VoiceChatClientEventHandler {
	
	@SubscribeEvent
	public static void render(RenderGameOverlayEvent.Post event) {
		final Minecraft minecraft = Minecraft.getInstance();
		if (event.getType() != ElementType.ALL || minecraft.gameSettings.showDebugInfo) {
			return;
		}
		RenderOverlayVoiceChat.draw();
	}
	
	@SubscribeEvent
	public static void on(GuiScreenEvent.InitGuiEvent.Post event) {
		final Screen gui = event.getGui();
		if (gui instanceof IngameMenuScreen) {
			event.addWidget(new ScalableButton(gui.width - 103, 1 + (MusicPlayerIntegrationUtil.isShowIngameMenueOverlay() ? 15 : 0), 102, 15, new StringTextComponent("Voicechat"), 0.7F, button -> {
				gui.getMinecraft().displayGuiScreen(new VoiceChatSettingsGui());
			}));
		}
	}
}
