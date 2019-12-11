package info.u_team.voice_chat.client;

import java.net.SocketException;
import java.util.*;
import java.util.concurrent.*;

import info.u_team.voice_chat.client.musicplayer.MusicPlayerVoiceClient;
import net.minecraftforge.fml.ModList;

public class VoiceClientManager {
	
	public static final Executor EXECUTOR = Executors.newCachedThreadPool();
	private static final Timer TIMER = new Timer(true);
	
	private static VoiceClient CLIENT;
	
	private static TimerTask TIMER_TASK;
	
	public static synchronized void start(int port, byte[] secret) {
		try {
			CLIENT = getClient(port, secret);
			TIMER.schedule(TIMER_TASK = new TimerTask() {
				
				@Override
				public void run() {
					TalkingList.removeAllThatAreInactiveFor200ms();
				}
			}, 200, 200);
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
	}
	
	public static synchronized void stop() {
		if (CLIENT != null) {
			CLIENT.close();
			CLIENT = null;
		}
		if (TIMER_TASK != null) {
			TIMER_TASK.cancel();
			TIMER_TASK = null;
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
	
	private static VoiceClient getClient(int port, byte[] secret) throws SocketException {
		if (ModList.get().isLoaded("musicplayer")) {
			return new MusicPlayerVoiceClient(port, secret);
		}
		return new VoiceClient(port, secret);
	}
	
}
