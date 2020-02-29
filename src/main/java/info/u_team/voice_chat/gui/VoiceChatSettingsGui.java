package info.u_team.voice_chat.gui;

import info.u_team.voice_chat.audio.*;
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
		
		final SpeakerHandler speakerHandler = SpeakerManager.getHandler();
		speakerMixerDeviceList = new VoiceChatSettingsGuiMixerDeviceList(width - 24, height, 110, 160, 12, width - 12, () -> SpeakerData.SPEAKER_INFO, speakerHandler::getSpeaker, speakerHandler::setSpeaker);
		children.add(speakerMixerDeviceList);
	}
	
	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		renderDirtBackground(0);
		speakerMixerDeviceList.render(mouseX, mouseY, partialTicks);
		microMixerDeviceList.render(mouseX, mouseY, partialTicks);
		font.drawString("Select microphone", 13, 12, 0xFFFFFF);
		font.drawString("Select speaker", 13, 92, 0xFFFFFF);
		super.render(mouseX, mouseY, partialTicks);
	}
	
}
