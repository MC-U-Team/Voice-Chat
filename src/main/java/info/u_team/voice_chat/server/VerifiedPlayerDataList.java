package info.u_team.voice_chat.server;

import java.net.InetAddress;
import java.util.*;

import net.minecraft.entity.player.ServerPlayerEntity;

public class VerifiedPlayerDataList {
	
	public static final Map<UUID, PlayerData> MAP = new HashMap<UUID, PlayerData>();
	
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
	
	public static class PlayerData {
		
		private final InetAddress address;
		private final int port;
		
		public PlayerData(InetAddress address, int port) {
			super();
			this.address = address;
			this.port = port;
		}
		
		public InetAddress getAddress() {
			return address;
		}
		
		public int getPort() {
			return port;
		}
	}
	
}
