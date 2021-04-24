package info.u_team.voice_chat.client;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import info.u_team.voice_chat.audio_client.util.ThreadUtil;

public class TalkingManager {
	
	private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor(ThreadUtil.createDaemonFactory("talking manager cleanup"));
	
	private static final Map<UUID, Long> MAP = new ConcurrentHashMap<>();
	
	private static Future<?> TASK;
	
	public static synchronized void addOrUpdate(UUID uuid) {
		MAP.put(uuid, System.currentTimeMillis());
	}
	
	public static synchronized void removeAllThatAreInactiveFor200ms() {
		final long currentTime = System.currentTimeMillis();
		MAP.entrySet().removeIf(entry -> currentTime - entry.getValue() > 200);
	}
	
	public static Set<UUID> getTalkers() {
		return Collections.unmodifiableSet(MAP.keySet());
	}
	
	public static synchronized void clear() {
		MAP.clear();
	}
	
	public static synchronized void start() {
		TASK = EXECUTOR.scheduleWithFixedDelay(TalkingManager::removeAllThatAreInactiveFor200ms, 100, 100, TimeUnit.MILLISECONDS);
	}
	
	public static synchronized void stop() {
		clear();
		if (TASK != null) {
			TASK.cancel(true);
			TASK = null;
		}
	}
	
	public static boolean isRunning() {
		return TASK != null;
	}
}
