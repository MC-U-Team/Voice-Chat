package info.u_team.voice_chat.audio_client.speaker;

import javax.sound.sampled.*;

import info.u_team.voice_chat.audio_client.util.AudioUtil;

public class SpeakerLineInfo {
	
	private final SourceDataLine line;
	
	private long lastAccessed;
	
	private boolean masterVolumeControlFound;
	private int multiplier;
	
	public SpeakerLineInfo(SourceDataLine line) {
		this.line = line;
		lastAccessed = System.currentTimeMillis();
	}
	
	public void setMasterVolume(int volume) {
		if (line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
			((FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN)).setValue(20F * (float) Math.log10(volume / 100F));
			masterVolumeControlFound = true;
		} else {
			multiplier = AudioUtil.calculateVolumeMultiplier(volume);
		}
	}
	
	public SourceDataLine getSourceDataLine() {
		lastAccessed = System.currentTimeMillis();
		return line;
	}
	
	public long getLastAccessed() {
		return lastAccessed;
	}
	
	public boolean isMasterVolumeControlFound() {
		return masterVolumeControlFound;
	}
	
	public int getMultiplier() {
		return multiplier;
	}
	
}
