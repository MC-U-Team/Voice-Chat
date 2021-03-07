package info.u_team.voice_chat.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import info.u_team.u_team_core.gui.elements.*;
import info.u_team.voice_chat.audio.MicroHandler;
import info.u_team.voice_chat.audio.MicroManager;
import info.u_team.voice_chat.audio.SpeakerHandler;
import info.u_team.voice_chat.audio.SpeakerManager;
import info.u_team.voice_chat.audio_client.micro.MicroData;
import info.u_team.voice_chat.audio_client.speaker.SpeakerData;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

public class VoiceChatSettingsGui extends Screen {
	
	private VoiceChatSettingsGuiMixerDeviceList microMixerDeviceList;
	private VoiceChatSettingsGuiMixerDeviceList speakerMixerDeviceList;
	
	public VoiceChatSettingsGui() {
		super(new StringTextComponent("voicechatsettings"));
	}
	
	@Override
	protected void init() {
		final MicroHandler microHandler = MicroManager.getHandler();
		microMixerDeviceList = new VoiceChatSettingsGuiMixerDeviceList(width - 24, 110, 30, 80, 12, width - 12, () -> MicroData.MIC_INFO, microHandler::getMicro, microHandler::setMicro);
		children.add(microMixerDeviceList);
		
		StringTextComponent volume = new StringTextComponent("Volume: ");
		StringTextComponent empty = new StringTextComponent("");
		addButton(new ScalableSlider(180, 9, width - 192, 15, volume, empty, 0, 150, microHandler.getVolume(), false, true, false, 0.7F, slider -> {
			microHandler.setVolume(slider.getValueInt());
		}));
		
		final SpeakerHandler speakerHandler = SpeakerManager.getHandler();
		speakerMixerDeviceList = new VoiceChatSettingsGuiMixerDeviceList(width - 24, height, 110, 160, 12, width - 12, () -> SpeakerData.SPEAKER_INFO, speakerHandler::getSpeaker, speakerHandler::setSpeaker);
		children.add(speakerMixerDeviceList);
		
		addButton(new ScalableSlider(180, 89, width - 192, 15, volume, empty, 0, 150, speakerHandler.getVolume(), false, true, false, 0.7F, slider -> {
			speakerHandler.setVolume(slider.getValueInt());
		}));
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
		renderDirtBackground(0);
		speakerMixerDeviceList.render(stack, mouseX, mouseY, partialTicks);
		microMixerDeviceList.render(stack, mouseX, mouseY, partialTicks);
		font.drawString(stack, "Select microphone", 13, 12, 0xFFFFFF);
		font.drawString(stack, "Select speaker", 13, 92, 0xFFFFFF);
		super.render(stack, mouseX, mouseY, partialTicks);
	}
	
}
