package info.u_team.voice_chat.audio;

public class MicroManager {
	
	private static MicroHandler MICRO;
	
	public synchronized static void start() {
		MICRO = new MicroHandler();
	}
	
	public synchronized static void stop() {
		if (MICRO != null) {
			MICRO.close();
			MICRO = null;
		}
	}
	
	public static boolean isRunning() {
		return MICRO != null;
	}
	
	public static MicroHandler getHandler() {
		return MICRO;
	}
	
}
