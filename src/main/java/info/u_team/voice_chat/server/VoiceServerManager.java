package info.u_team.voice_chat.server;

import java.net.SocketException;
import java.util.concurrent.*;

public class VoiceServerManager {
	
	public static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
	
	private static VoiceServer SERVER;
	
	public synchronized static void start() {
		try {
			SERVER = new VoiceServer(EXECUTOR);
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
	}
	
	public synchronized static void stop() {
		if (SERVER != null) {
			SERVER.close();
			SERVER = null;
		}
	}
	
	public static boolean isRunning() {
		return SERVER != null;
	}
	
	public static VoiceServer getServer() {
		return SERVER;
	}
	
}
