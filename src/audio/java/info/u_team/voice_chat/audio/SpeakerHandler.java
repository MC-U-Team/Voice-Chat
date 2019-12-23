package info.u_team.voice_chat.audio;

import info.u_team.voice_chat.audio_client.api.NoExceptionCloseable;
import info.u_team.voice_chat.audio_client.speaker.*;

public class SpeakerHandler implements NoExceptionCloseable {
	
	private final SpeakerData data;
	private final SpeakerPlayer player;
	
	public SpeakerHandler() {
		data = new SpeakerData("");
		player = new SpeakerPlayer(data);
	}
	
	public void receiveVoicePacket(int id, byte[] opusPacket) {
		player.accept(id, opusPacket);
	}
	
	public void setSpeaker(String mixer) {
		data.setMixer(mixer);
	}
	
	@Override
	public void close() {
		player.close();
		data.close();
	}
	
}
