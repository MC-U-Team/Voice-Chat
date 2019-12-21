package info.u_team.voice_chat.client;

import java.util.UUID;

import com.google.common.collect.*;

public class PlayerIDManager {
	
	private static final BiMap<UUID, Short> MAP = HashBiMap.create();
	
	public static void addAllPlayers(UUID[] uuids, short[] ids) {
		for (int index = 0; index < uuids.length; index++) {
			MAP.put(uuids[index], ids[index]);
		}
	}
	
	public static void addPlayer(UUID uuid, short id) {
		MAP.put(uuid, id);
	}
	
	public static void removePlayer(UUID uuid) {
		MAP.remove(uuid);
	}
	
	public static UUID getPlayerByID(short id) {
		return MAP.inverse().get(id);
	}
	
	public static void clear() {
		MAP.clear();
	}
}
