package info.u_team.voice_chat.client;

import java.net.SocketException;
import java.util.concurrent.*;

import info.u_team.voice_chat.audio_client.util.ThreadUtil;

public class VoiceClientManager {
	
	public static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(ThreadUtil.createDaemonFactory("voice client"));
	
	private static VoiceClient CLIENT;
	
	public static synchronized void start(int port, byte[] secret) {
		try {
			CLIENT = new VoiceClient(EXECUTOR, port, secret);
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
	
	public static boolean isRunning() {
		return CLIENT != null;
	}
	
	public static VoiceClient getClient() {
		return CLIENT;
	}
	
	public static void setHandshakeDone() {
		if (CLIENT != null) {
			CLIENT.setHandshakeDone();
		}
	}
}
