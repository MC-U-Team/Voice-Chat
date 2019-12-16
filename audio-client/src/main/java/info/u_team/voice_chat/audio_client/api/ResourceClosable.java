package info.u_team.voice_chat.audio_client.api;

public interface ResourceClosable extends AutoCloseable {
	
	@Override
	void close();
	
}
