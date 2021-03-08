package info.u_team.voice_chat.server;

import java.net.SocketException;
import java.util.concurrent.*;

import info.u_team.voice_chat.audio_client.util.ThreadUtil;

public class VoiceServerManager {
	
	public static final ExecutorService EXECUTOR = Executors.newCachedThreadPool(ThreadUtil.createDaemonFactory("voice server"));
	
	private static VoiceServer SERVER;
	
	public synchronized static void start() {
		try {
			SERVER = new VoiceServer(EXECUTOR);
		} catch (final SocketException ex) {
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
