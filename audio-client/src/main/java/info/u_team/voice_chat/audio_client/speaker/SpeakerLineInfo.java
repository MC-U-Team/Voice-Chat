package info.u_team.voice_chat.audio_client.speaker;

import javax.sound.sampled.*;

public class SpeakerLineInfo {
	
	private final SourceDataLine line;
	
	private long lastAccessed;
	
	public SpeakerLineInfo(SourceDataLine line) {
		this.line = line;
		lastAccessed = System.currentTimeMillis();
	}
	
	public void setGain(int volume) {
		if (line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			((FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN)).setValue(20F * (float) Math.log10(volume / 100F));
		}
	}
	
	public SourceDataLine getSourceDataLine() {
		lastAccessed = System.currentTimeMillis();
		return line;
	}
	
	public long getLastAccessed() {
		return lastAccessed;
	}
	
}
