package info.u_team.voice_chat.server;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.entity.player.ServerPlayerEntity;

public class VerifiedPlayerManager {
	
	private static final Map<UUID, PlayerData> MAP = new HashMap<UUID, PlayerData>();
	
	public static synchronized void addPlayer(ServerPlayerEntity player, PlayerData playerData) {
		if (!MAP.containsKey(player.getUniqueID())) {
			MAP.put(player.getUniqueID(), playerData);
		}
	}
	
	public static synchronized void removePlayer(ServerPlayerEntity player) {
		MAP.remove(player.getUniqueID());
	}
	
	public static PlayerData getPlayerData(ServerPlayerEntity player) {
		return MAP.get(player.getUniqueID());
	}
	
	public static boolean hasPlayerData(ServerPlayerEntity player) {
		return MAP.containsKey(player.getUniqueID());
	}
	
	public static Map<UUID, PlayerData> getMap() {
		return Collections.unmodifiableMap(MAP);
	}
	
	public static class PlayerData {
		
		private static final AtomicInteger COUNTER = new AtomicInteger();
		
		private final InetSocketAddress address;
		private final short id;
		
		public PlayerData(InetSocketAddress address) {
			this.address = address;
			// We don't care about collisions because there will never be more than 32k players connected to a voice server at the
			// same time
			id = (short) COUNTER.getAndIncrement();
		}
		
		public InetSocketAddress getAddress() {
			return address;
		}
		
		public short getId() {
			return id;
		}
	}
	
}
