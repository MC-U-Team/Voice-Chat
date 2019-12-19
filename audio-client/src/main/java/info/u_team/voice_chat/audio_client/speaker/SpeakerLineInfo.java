package info.u_team.voice_chat.audio_client.speaker;

import javax.sound.sampled.SourceDataLine;

public class SpeakerLineInfo {
	
	private final SourceDataLine line;
	
	private long lastAccessed;
	
	public SpeakerLineInfo(SourceDataLine line) {
		this.line = line;
		lastAccessed = System.currentTimeMillis();
	}
	
	public SourceDataLine getSourceDataLine() {
		lastAccessed = System.currentTimeMillis();
		return line;
	}
	
	public long getLastAccessed() {
		return lastAccessed;
	}
	
}
