package info.u_team.voice_chat.audio_client.util;

import java.nio.*;
import java.util.*;

import javax.sound.sampled.*;

public class AudioUtil {
	
	public static Mixer findMixer(String name, Line.Info lineInfo) {
		Mixer defaultMixer = null;
		for (final Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
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
		for (final Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
			final Mixer mixer = AudioSystem.getMixer(mixerInfo);
			if (mixer.isLineSupported(lineInfo)) {
				list.add(mixerInfo.getName());
			}
		}
		return list;
	}
	
	public static boolean hasLinesOpen(Mixer mixer) {
		return mixer.getSourceLines().length != 0 || mixer.getTargetLines().length != 0;
	}
	
	public static int calculateVolumeMultiplier(int volume) {
		return (int) ((float) Math.tan(volume * 0.0079F) * 10000);
	}
	
	public static void changeVolume(byte[] pcm, int volume, int multiplier) {
		if (volume == 100) {
			return;
		}
		changeVolume(pcm, multiplier);
	}
	
	public static void changeVolume(byte[] pcm, int multiplier) {
		final ShortBuffer buffer = ByteBuffer.wrap(pcm).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
		for (int index = buffer.position(); index < buffer.limit(); index++) {
			buffer.put(index, (short) Math.max(Short.MIN_VALUE, Math.min(Short.MAX_VALUE, buffer.get(index) * multiplier / 10000)));
		}
	}
	
}
