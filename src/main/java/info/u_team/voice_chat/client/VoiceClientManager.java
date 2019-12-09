package info.u_team.voice_chat.client;

import java.net.SocketException;
import java.util.*;
import java.util.concurrent.*;

public class VoiceClientManager {
	
	public static final Executor EXECUTOR = Executors.newCachedThreadPool();
	private static final Timer TIMER = new Timer(true);
	
	private static VoiceClient CLIENT;
	
	public static synchronized void start(int port, byte[] secret) {
		try {
			CLIENT = new VoiceClient(port, secret);
			TIMER.schedule(new TimerTask() {
				
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
		TIMER.cancel();
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
