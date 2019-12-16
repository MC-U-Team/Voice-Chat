package info.u_team.voice_chat.audio_client.micro;

import javax.sound.sampled.*;

import info.u_team.voice_chat.audio_client.api.ResourceClosable;

public class MicroData implements ResourceClosable {
	
	private static final AudioFormat FORMAT = new AudioFormat(48000, 16, 2, true, false);
	private static final DataLine.Info MIC_INFO = new DataLine.Info(TargetDataLine.class, FORMAT);
	
	private TargetDataLine targetLine;
	
	public MicroData(String microName) {
		setTargetLine(microName);
	}
	
	public void setTargetLine(String microName) {
		openTargetLine(findMicrophoneOrUseDefault(microName));
	}
	
	private void openTargetLine(TargetDataLine newLine) {
		if (targetLine != null) {
			targetLine.close();
		}
		targetLine = newLine;
		try {
			targetLine.open(FORMAT, 960 * 2 * 2 * 4);
			targetLine.start();
		} catch (LineUnavailableException ex) {
			targetLine = null;
		}
	}
	
	private TargetDataLine findMicrophoneOrUseDefault(String name) {
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
	
	public boolean isAvailable() {
		return targetLine != null;
	}
	
	public void flush() {
		if (isAvailable()) {
			targetLine.flush();
		}
	}
	
	public byte[] read(byte[] array) {
		if (isAvailable()) {
			targetLine.read(array, 0, array.length);
		}
		return array;
	}
	
	@Override
	public void close() {
		if (isAvailable()) {
			targetLine.close();
		}
	}
	
}
