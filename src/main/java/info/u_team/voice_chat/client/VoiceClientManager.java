package info.u_team.voice_chat.client;

import java.net.SocketException;

public class VoiceClientManager {
	
	private static VoiceClient CLIENT;
	
	public static synchronized void start(int port, byte[] secret) {
		try {
			CLIENT = new VoiceClient(port, secret);
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
	}
	
	public static synchronized void stop() {
		if (CLIENT != null) {
			CLIENT.close();
			CLIENT = null;
		}
	}
	
	public static synchronized void setHandshakeDone() {
		if (CLIENT != null) {
			CLIENT.setHandshakeDone();
		}
	}
	
	public static synchronized boolean isRunning() {
		return CLIENT != null;
	}
	
}
