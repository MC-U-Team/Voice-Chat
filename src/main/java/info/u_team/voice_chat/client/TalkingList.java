package info.u_team.voice_chat.client;

import java.util.*;

public class TalkingList {
	
	private static final Map<UUID, Long> MAP = new HashMap<>();
	
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
}
