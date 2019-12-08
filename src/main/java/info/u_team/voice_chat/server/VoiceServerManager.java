package info.u_team.voice_chat.server;

import java.net.SocketException;
import java.util.concurrent.*;

public class VoiceServerManager {
	
	public static final Executor EXECUTOR = Executors.newCachedThreadPool();
	
	private static VoiceServer SERVER;
	
	public synchronized static void start() {
		try {
			SERVER = new VoiceServer();
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
	
	public synchronized static boolean isRunning() {
		return SERVER != null;
	}
	
}
