package info.u_team.voice_chat.client;

import java.util.UUID;

import com.google.common.collect.*;

public class PlayerIDManager {
	
	private static final BiMap<UUID, Short> MAP = HashBiMap.create();
	
	public static synchronized void addAllPlayer(UUID[] uuids, short[] ids) {
		for (int index = 0; index < uuids.length; index++) {
			MAP.put(uuids[index], ids[index]);
		}
	}
	
	public static synchronized void addPlayer(UUID uuid, short id) {
		MAP.put(uuid, id);
	}
	
	public static synchronized void removePlayer(UUID uuid) {
		MAP.remove(uuid);
	}
	
	public static synchronized UUID getPlayerByID(short id) {
		return MAP.inverse().get(id);
	}
	
	public static synchronized void clear() {
		MAP.clear();
	}
}
