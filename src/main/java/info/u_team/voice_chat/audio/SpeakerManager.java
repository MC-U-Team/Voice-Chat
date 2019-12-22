package info.u_team.voice_chat.audio;

public class SpeakerManager {
	
	private static SpeakerHandler SPEAKER;
	
	public synchronized static void start() {
		SPEAKER = new SpeakerHandler();
	}
	
	public synchronized static void stop() {
		if (SPEAKER != null) {
			SPEAKER.close();
			SPEAKER = null;
		}
	}
	
	public static boolean isRunning() {
		return SPEAKER != null;
	}
	
	public static SpeakerHandler getHandler() {
		return SPEAKER;
	}
	
}
