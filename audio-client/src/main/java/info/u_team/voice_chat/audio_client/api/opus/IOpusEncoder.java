package info.u_team.voice_chat.audio_client.api.opus;

import info.u_team.voice_chat.audio_client.api.NoExceptionCloseable;

public interface IOpusEncoder extends NoExceptionCloseable {
	
	byte[] encode(byte[] pcm);
	
	byte[] silence();
	
}
