package info.u_team.voice_chat.gui;

import java.util.function.*;

import javax.sound.sampled.Line;

import info.u_team.u_team_core.gui.elements.ScrollableList;
import info.u_team.voice_chat.audio_client.util.AudioUtil;

public class VoiceChatSettingsGuiMixerDeviceList extends ScrollableList<VoiceChatSettingsGuiMixerDeviceListEntry> {
	
	private final Consumer<String> mixerSetter;
	
	public VoiceChatSettingsGuiMixerDeviceList(int width, int height, int top, int bottom, int left, int right, Supplier<Line.Info> lineInfoGetter, Supplier<String> mixerGetter, Consumer<String> mixerSetter) {
		super(width, height, top, bottom, left, right, 20, 20);
		this.mixerSetter = mixerSetter;
		
		func_244606_c(false);
		setShouldUseScissor(true);
		setShouldRenderTransparentBorder(true);
		
		AudioUtil.findAudioDevices(lineInfoGetter.get()).stream().map(VoiceChatSettingsGuiMixerDeviceListEntry::new).peek(entry -> {
			if (entry.getMixerName().equals(mixerGetter.get())) {
				super.setSelected(entry);
			}
		}).forEach(this::addEntry);
	}
	
	@Override
	public void setSelected(VoiceChatSettingsGuiMixerDeviceListEntry entry) {
		super.setSelected(entry);
		mixerSetter.accept(entry.getMixerName());
	}
}
