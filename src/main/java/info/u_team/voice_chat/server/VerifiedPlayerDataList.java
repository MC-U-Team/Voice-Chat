package info.u_team.voice_chat.server;

import java.net.*;
import java.util.*;
import java.util.function.BiConsumer;

import net.minecraft.entity.player.ServerPlayerEntity;

public class VerifiedPlayerDataList {
	
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
	
	public static void iterate(BiConsumer<UUID, PlayerData> consumer) {
		MAP.forEach(consumer);
	}
	
	public static class PlayerData {
		
		private final InetSocketAddress address;
		
		public PlayerData(InetSocketAddress address) {
			super();
			this.address = address;
		}
		
		public InetSocketAddress getAddress() {
			return address;
		}
	}
	
}
