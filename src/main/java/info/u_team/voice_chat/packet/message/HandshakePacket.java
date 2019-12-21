package info.u_team.voice_chat.packet.message;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

import info.u_team.voice_chat.init.VoiceChatNetworks;
import info.u_team.voice_chat.message.*;
import info.u_team.voice_chat.packet.PacketRegistry.Context;
import info.u_team.voice_chat.server.VerifiedPlayerManager;
import info.u_team.voice_chat.server.VerifiedPlayerManager.PlayerData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

public class HandshakePacket {
	
	public static ByteBuffer encode(HandshakePacket message) {
		return ByteBuffer.allocate(0);
	}
	
	public static HandshakePacket decode(ByteBuffer buffer) {
		return new HandshakePacket();
	}
	
	public static class Handler {
		
		public static void handle(HandshakePacket message, Supplier<Context> contextSupplier) {
			final Context context = contextSupplier.get();
			
			if (!context.hasPlayer()) {
				return;
			}
			
			final ServerPlayerEntity player = context.getPlayer();
			
			if (!VerifiedPlayerManager.hasPlayerData(player)) {
				final PlayerData data = new PlayerData(context.getAddress());
				VerifiedPlayerManager.addPlayer(player, data);
				VoiceChatNetworks.NETWORK.send(PacketDistributor.PLAYER.with(() -> player), new HandshakeDoneMessage());
				VoiceChatNetworks.NETWORK.send(PacketDistributor.ALL.noArg(), new PlayerIDMessage(false, player.getUniqueID(), data.getId()));
			}
		}
	}
}
