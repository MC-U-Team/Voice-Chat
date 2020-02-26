package info.u_team.voice_chat.audio_client.micro;

import javax.sound.sampled.*;

import info.u_team.voice_chat.audio_client.api.NoExceptionCloseable;
import info.u_team.voice_chat.audio_client.util.AudioUtil;

public class MicroData implements NoExceptionCloseable {
	
	private static final AudioFormat FORMAT = new AudioFormat(48000, 16, 2, true, false);
	private static final DataLine.Info MIC_INFO = new DataLine.Info(TargetDataLine.class, FORMAT);
	
	private Mixer mixer;
	private TargetDataLine targetLine;
	
	public MicroData(String microName) {
		setMixer(microName);
	}
	
	private boolean createLine() {
		if (mixer != null) {
			try {
				final TargetDataLine line = (TargetDataLine) mixer.getLine(MIC_INFO);
				line.open(FORMAT, 960 * 2 * 2 * 4);
				line.start();
				targetLine = line;
				return true;
			} catch (LineUnavailableException ex) {
			}
		}
		return false;
	}
	
	private void closeLine() {
		if (targetLine != null) {
			targetLine.flush();
			targetLine.stop();
			targetLine.close();
		}
	}
	
	public void setMixer(String name) {
		closeLine();
		if (mixer != null) {
			if (!AudioUtil.hasLinesOpen(mixer)) {
				mixer.close();
			}
		}
		mixer = AudioUtil.findMixer(name, MIC_INFO);
	}
	
	public boolean isAvailable() {
		if (mixer != null) {
			if (targetLine != null) {
				return targetLine.isOpen();
			} else {
				return createLine();
			}
		}
		return false;
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
		closeLine();
		if (mixer != null) {
			if (!AudioUtil.hasLinesOpen(mixer)) {
				mixer.close();
			}
		}
	}
	
}
