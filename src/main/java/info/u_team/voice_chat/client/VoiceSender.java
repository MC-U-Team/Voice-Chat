package info.u_team.voice_chat.client;

import javax.sound.sampled.*;

import info.u_team.voice_chat.config.CommonConfig;

public class VoiceSender extends VoiceInfo {
	
	private static final DataLine.Info MIC_INFO = new DataLine.Info(TargetDataLine.class, FORMAT);
	
	private TargetDataLine targetLine;
	
	public VoiceSender() {
	}
	
	public void setTargetLine(TargetDataLine newLine) throws LineUnavailableException {
		if (targetLine != null) {
			targetLine.close();
		}
		targetLine = newLine;
		targetLine.open(FORMAT);
		targetLine.start();
		
		CommonConfig.getInstance().portValue.set(1);
	}
	
	public TargetDataLine findMicrophoneOrUseDefault(String name) {
		try {
			for (Mixer.Info info : AudioSystem.getMixerInfo()) {
				final Mixer mixer = AudioSystem.getMixer(info);
				if (mixer.isLineSupported(MIC_INFO) && info.getName().equals(name)) {
					return (TargetDataLine) mixer.getLine(MIC_INFO);
				}
			}
			return (TargetDataLine) AudioSystem.getLine(MIC_INFO);
		} catch (LineUnavailableException ex) {
			return null;
		}
		
	}
	
}
