package info.u_team.voice_chat.musicplayer_integration.handler;

import info.u_team.u_team_core.gui.elements.ScalableActivatableButton;
import info.u_team.u_team_core.util.RGBA;
import info.u_team.voice_chat.VoiceChatMod;
import info.u_team.voice_chat.gui.VoiceChatSettingsGui;
import info.u_team.voice_chat.musicplayer_integration.MusicPlayerIntegration;
import info.u_team.voice_chat.musicplayer_integration.util.MusicPlayerIntegrationUtil;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = VoiceChatMod.MODID, value = Dist.CLIENT, bus = Bus.FORGE)
public class MusicPlayerIntegrationClientHandler {
	
	@SubscribeEvent
	public static void on(GuiScreenEvent.InitGuiEvent.Post event) {
		if (!MusicPlayerIntegrationUtil.isMusicPlayerInstalled()) {
			return;
		}
		final Screen gui = event.getGui();
		if (gui instanceof VoiceChatSettingsGui) {
			final ScalableActivatableButton streamButton = new ScalableActivatableButton(13, 190, gui.width - 24, 15, new StringTextComponent("Stream music to other players"), 1, MusicPlayerIntegration.getInstance().isShouldStream(), new RGBA(0x80FF00FF));
			streamButton.setPressable(() -> {
				MusicPlayerIntegration.getInstance().setShouldStream(!streamButton.isActivated());
				streamButton.setActivated(!streamButton.isActivated());
			});
			event.addWidget(streamButton);
		}
	}
	
	@SubscribeEvent
	public static void on(GuiScreenEvent.DrawScreenEvent.Post event) {
		if (!MusicPlayerIntegrationUtil.isMusicPlayerInstalled()) {
			return;
		}
		final Screen gui = event.getGui();
		if (gui instanceof VoiceChatSettingsGui) {
			gui.getMinecraft().fontRenderer.drawString(event.getMatrixStack(), "Select speaker", 13, 172, 0xFFFFFF);
		}
	}
	
}
