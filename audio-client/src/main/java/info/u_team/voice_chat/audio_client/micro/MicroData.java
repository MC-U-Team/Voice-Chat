package info.u_team.voice_chat.audio_client.micro;

import java.nio.*;

import javax.sound.sampled.*;

import info.u_team.voice_chat.audio_client.api.NoExceptionCloseable;
import info.u_team.voice_chat.audio_client.util.AudioUtil;

public class MicroData implements NoExceptionCloseable {
	
	private static final AudioFormat FORMAT = new AudioFormat(48000, 16, 2, true, false);
	private static final DataLine.Info MIC_INFO = new DataLine.Info(TargetDataLine.class, FORMAT);
	
	private Mixer mixer;
	private TargetDataLine targetLine;
	
	private double volume;
	
	public MicroData(String microName) {
		setMixer(microName);
		volume = 1;
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
	
	public void setVolume(double volume) {
		this.volume = volume;
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
			adjustVolume(array, volume);
		}
		return array;
	}
	
	private void adjustVolume(byte[] pcm, double volume) {
		if (Math.abs(volume - 1) < 0.001) {
			return;
		}
		final ShortBuffer shortBuffer = ByteBuffer.wrap(pcm).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
		for (int index = 0; index < shortBuffer.capacity(); index++) {
			shortBuffer.put(index, (short) (shortBuffer.get(index) * volume));
		}
		return;
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
