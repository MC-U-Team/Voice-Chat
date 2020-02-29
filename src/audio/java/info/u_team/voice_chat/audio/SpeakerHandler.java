package info.u_team.voice_chat.audio;

import info.u_team.voice_chat.audio_client.api.NoExceptionCloseable;
import info.u_team.voice_chat.audio_client.speaker.*;
import info.u_team.voice_chat.config.ClientConfig;

public class SpeakerHandler implements NoExceptionCloseable {
	
	private final ClientConfig config;
	
	private final SpeakerData data;
	private final SpeakerPlayer player;
	
	public SpeakerHandler() {
		config = ClientConfig.getInstance();
		data = new SpeakerData(config.speakerValue.get(), config.speakerVolumeValue.get());
		player = new SpeakerPlayer(data);
	}
	
	public void receiveVoicePacket(int id, byte[] opusPacket) {
		player.accept(id, opusPacket);
	}
	
	public String getSpeaker() {
		return data.getMixer();
	}
	
	public void setSpeaker(String mixer) {
		data.setMixer(mixer);
		config.speakerValue.set(mixer);
		ClientConfig.CONFIG.save();
	}
	
	public int getVolume() {
		return data.getVolume();
	}
	
	public void setVolume(int volume) {
		data.setVolume(volume);
		config.speakerVolumeValue.set(volume);
		ClientConfig.CONFIG.save();
	}
	
	@Override
	public void close() {
		player.close();
		data.close();
	}
	
}
