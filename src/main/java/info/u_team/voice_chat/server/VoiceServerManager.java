package info.u_team.voice_chat.server;

import java.net.SocketException;

public class VoiceServerManager {
	
	private static VoiceServer SERVER;
	
	public static void start() {
		try {
			SERVER = new VoiceServer();
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void stop() {
		if (SERVER != null) {
			SERVER.close();
			SERVER = null;
		}
	}
	
	public static boolean isRunning() {
		return SERVER != null;
	}
	
}
