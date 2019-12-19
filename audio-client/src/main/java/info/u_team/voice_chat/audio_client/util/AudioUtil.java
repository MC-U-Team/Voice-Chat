package info.u_team.voice_chat.audio_client.util;

import java.util.*;

import javax.sound.sampled.*;

public class AudioUtil {
	
	public static Mixer findMixer(String name, Line.Info lineInfo) {
		Mixer defaultMixer = null;
		for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
			final Mixer mixer = AudioSystem.getMixer(mixerInfo);
			if (mixer.isLineSupported(lineInfo)) {
				if (mixerInfo.getName().equals(name)) {
					return mixer;
				}
				if (defaultMixer == null) {
					defaultMixer = mixer;
				}
			}
		}
		return defaultMixer;
	}
	
	public static List<String> findAudioDevices(Line.Info lineInfo) {
		final List<String> list = new ArrayList<>();
		for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
			final Mixer mixer = AudioSystem.getMixer(mixerInfo);
			if (mixer.isLineSupported(lineInfo)) {
				list.add(mixerInfo.getName());
			}
		}
		return list;
	}
	
}
