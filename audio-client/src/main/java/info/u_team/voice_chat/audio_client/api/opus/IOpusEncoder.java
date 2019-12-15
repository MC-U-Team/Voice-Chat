package info.u_team.voice_chat.audio_client.api.opus;

public interface IOpusEncoder {
	
	byte[] encode(byte[] pcm);
	
	byte[] silence();
	
}
