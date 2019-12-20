package info.u_team.voice_chat.server;

import java.util.*;

import com.google.common.collect.*;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.*;

public class PlayerSecretManager {
	
	private static final BiMap<UUID, Secret> MAP = HashBiMap.create();
	private static final Random RANDOM = new Random();
	
	public static synchronized void addPlayer(ServerPlayerEntity player) {
		if (MAP.containsKey(player.getUniqueID())) {
			return;
		}
		MAP.put(player.getUniqueID(), findFreeSecret());
	}
	
	public static synchronized void removePlayer(ServerPlayerEntity player) {
		MAP.remove(player.getUniqueID());
	}
	
	public static ServerPlayerEntity getPlayerBySecret(byte[] secret) {
		final MinecraftServer server = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
		return server.getPlayerList().getPlayerByUUID(MAP.inverse().get(new Secret(secret)));
	}
	
	public static byte[] getSecretByPlayer(ServerPlayerEntity player) {
		return MAP.get(player.getUniqueID()).getSecret();
	}
	
	private static Secret findFreeSecret() {
		final byte[] randomBytes = new byte[8];
		RANDOM.nextBytes(randomBytes);
		final Secret secret = new Secret(randomBytes);
		while (MAP.containsValue(secret)) {
			RANDOM.nextBytes(randomBytes);
		}
		return secret;
	}
	
	public static class Secret {
		
		private byte[] secret = new byte[8];
		
		private Secret(byte[] secret) {
			this.secret = secret;
		}
		
		public byte[] getSecret() {
			return Arrays.copyOf(secret, secret.length);
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(secret);
			return result;
		}
		
		@Override
		public boolean equals(Object object) {
			if (this == object) {
				return true;
			}
			if (object == null) {
				return false;
			}
			if (getClass() != object.getClass()) {
				return false;
			}
			final Secret other = (Secret) object;
			if (!Arrays.equals(secret, other.secret)) {
				return false;
			}
			return true;
		}
		
	}
}
