package info.u_team.voice_chat.audio_client.speaker;

import javax.sound.sampled.*;

public class SpeakerData {
	
	private static final AudioFormat FORMAT = new AudioFormat(48000, 16, 2, true, false);
	private static final DataLine.Info SPEAKER_INFO = new DataLine.Info(SourceDataLine.class, FORMAT);
	
	private SourceDataLine sourceLine;
	
	public SpeakerData(String speakerName) {
		setSourceLine(speakerName);
	}
	
	public void setSourceLine(String microName) {
		openSourceLine(findSpeakerOrUseDefault(microName));
	}
	
	private void openSourceLine(SourceDataLine newLine) {
		if (sourceLine != null) {
			sourceLine.close();
		}
		sourceLine = newLine;
		try {
			sourceLine.open(FORMAT, 960 * 2 * 2 * 4);
			sourceLine.start();
		} catch (LineUnavailableException ex) {
			sourceLine = null;
		}
	}
	
	private static SourceDataLine findSpeakerOrUseDefault(String name) {
		try {
			for (Mixer.Info info : AudioSystem.getMixerInfo()) {
				final Mixer mixer = AudioSystem.getMixer(info);
				if (mixer.isLineSupported(SPEAKER_INFO) && info.getName().equals(name)) {
					return (SourceDataLine) mixer.getLine(SPEAKER_INFO);
				}
			}
			return (SourceDataLine) AudioSystem.getLine(SPEAKER_INFO);
		} catch (LineUnavailableException ex) {
			return null;
		}
	}
	
	public boolean isAvailable() {
		return sourceLine != null;
	}
	
	public void flush() {
		if (isAvailable()) {
			sourceLine.flush();
		}
	}
	
	public byte[] write(byte[] array) {
		if (isAvailable()) {
			sourceLine.write(array, 0, array.length);
		}
		return array;
	}
	
	public int freeBuffer() {
		if (isAvailable()) {
			return sourceLine.available();
		}
		return 0;
	}
	
}
