package info.u_team.voice_chat.audio_client.speaker;

import java.util.*;

import javax.sound.sampled.*;

import info.u_team.voice_chat.audio_client.api.NoExceptionCloseable;
import info.u_team.voice_chat.audio_client.util.AudioUtil;

public class SpeakerData implements NoExceptionCloseable {
	
	private static final AudioFormat FORMAT = new AudioFormat(48000, 16, 2, true, false);
	private static final DataLine.Info SPEAKER_INFO = new DataLine.Info(SourceDataLine.class, FORMAT);
	
	private Mixer mixer;
	
	private final Map<Integer, SourceDataLine> sourceLines;
	
	public SpeakerData(String speakerName) {
		sourceLines = new HashMap<>();
		setMixer(speakerName);
	}
	
	private boolean createLine(int id) {
		if (mixer != null) {
			try {
				final SourceDataLine line = (SourceDataLine) mixer.getLine(SPEAKER_INFO);
				line.open(FORMAT, 960 * 2 * 2 * 4);
				line.start();
				sourceLines.put(id, line);
				return true;
			} catch (LineUnavailableException ex) {
			}
		}
		return false;
	}
	
	private void removeLine(int id) {
		final SourceDataLine line = sourceLines.remove(id);
		if (line != null) {
			line.flush();
			line.close();
		}
	}
	
	private void setMixer(String name) {
		mixer = AudioUtil.findMixer(name, SPEAKER_INFO);
	}
	
	public boolean isAvailable(int id) {
		if (mixer != null) {
			if (sourceLines.containsKey(id)) {
				return true;
			} else {
				return createLine(id);
			}
		}
		return false;
	}
	
	public void flush(int id) {
		if (isAvailable(id)) {
			sourceLines.get(id).flush();
		}
	}
	
	public byte[] write(int id, byte[] array) {
		if (isAvailable(id)) {
			sourceLines.get(id).write(array, 0, array.length);
		}
		return array;
	}
	
	public int freeBuffer(int id) {
		if (isAvailable(id)) {
			return sourceLines.get(id).available();
		}
		return 0;
	}
	
	@Override
	public void close() {
		sourceLines.values().forEach(Line::close);
		sourceLines.clear();
		if (mixer != null) {
			mixer.close();
		}
	}
	
}
