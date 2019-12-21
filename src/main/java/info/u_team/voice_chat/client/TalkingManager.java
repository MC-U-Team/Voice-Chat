package info.u_team.voice_chat.client;

import java.util.*;

public class TalkingManager {
	
	private static final Map<UUID, Long> MAP = new HashMap<>();
	
	public static void addOrUpdate(UUID uuid) {
		MAP.put(uuid, System.currentTimeMillis());
	}
	
	public static void removeAllThatAreInactiveFor200ms() {
		final long currentTime = System.currentTimeMillis();
		MAP.entrySet().removeIf(entry -> currentTime - entry.getValue() > 200);
	}
	
	public static Set<UUID> getTalkers() {
		return Collections.unmodifiableSet(MAP.keySet());
	}
	
	public static void clear() {
		MAP.clear();
	}
}
