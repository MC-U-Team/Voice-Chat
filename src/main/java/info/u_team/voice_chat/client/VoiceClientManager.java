package info.u_team.voice_chat.client;

import java.net.SocketException;

public class VoiceClientManager {
	
	private static VoiceClient CLIENT;
	
	public static void start(int port, byte[] secret) {
		try {
			CLIENT = new VoiceClient(port, secret);
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
	}
	
	public static void stop() {
		if (CLIENT != null) {
			CLIENT.close();
			CLIENT = null;
		}
	}
	
	public static void setHandshakeDone() {
		if (CLIENT != null) {
			CLIENT.setHandshakeDone();
		}
	}
	
	public static boolean isRunning() {
		return CLIENT != null;
	}
	
}
