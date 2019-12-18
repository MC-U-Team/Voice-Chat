package info.u_team.voice_chat.audio_client.api;

public interface NoExceptionCloseable extends AutoCloseable {
	
	@Override
	void close();
	
}
