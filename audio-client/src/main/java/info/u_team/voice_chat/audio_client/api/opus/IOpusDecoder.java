package info.u_team.voice_chat.audio_client.api.opus;

import info.u_team.voice_chat.audio_client.api.NoExceptionCloseable;

public interface IOpusDecoder extends NoExceptionCloseable {
	
	byte[] decoder(byte[] opus);
	
}
