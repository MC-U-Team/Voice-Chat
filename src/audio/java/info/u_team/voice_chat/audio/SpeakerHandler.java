package info.u_team.voice_chat.audio;

import info.u_team.voice_chat.audio_client.api.NoExceptionCloseable;
import info.u_team.voice_chat.audio_client.api.opus.IOpusDecoder;
import info.u_team.voice_chat.audio_client.opus.PcmOpusDecoder;
import info.u_team.voice_chat.audio_client.speaker.*;

public class SpeakerHandler implements NoExceptionCloseable {
	
	private static final IOpusDecoder DECORDER = new PcmOpusDecoder(48000, 2, 20);
	
	private final SpeakerData data;
	private final SpeakerPlayer player;
	
	public SpeakerHandler() {
		data = new SpeakerData("");
		player = new SpeakerPlayer(data, DECORDER);
	}
	
	public void receiveVoicePacket(int id, byte[] opusPacket) {
		player.accept(id, opusPacket);
	}
	
	@Override
	public void close() {
		player.close();
		data.close();
	}
	
}
