package info.u_team.voice_chat.audio;

import info.u_team.voice_chat.audio_client.api.NoExceptionCloseable;
import info.u_team.voice_chat.audio_client.speaker.*;
import info.u_team.voice_chat.config.ClientConfig;

public class SpeakerHandler implements NoExceptionCloseable {
	
	private final SpeakerData data;
	private final SpeakerPlayer player;
	
	public SpeakerHandler() {
		final ClientConfig config = ClientConfig.getInstance();
		data = new SpeakerData(config.speakerValue.get(), config.speakerVolumeValue.get());
		player = new SpeakerPlayer(data);
	}
	
	public void receiveVoicePacket(int id, byte[] opusPacket) {
		player.accept(id, opusPacket);
	}
	
	public void setSpeaker(String mixer) {
		data.setMixer(mixer);
	}
	
	public void setVolume(int volume) {
		data.setVolume(volume);
	}
	
	@Override
	public void close() {
		player.close();
		data.close();
	}
	
}
