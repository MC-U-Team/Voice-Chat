package info.u_team.voice_chat.packet.message;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

import info.u_team.voice_chat.packet.PacketRegistry.Context;
import info.u_team.voice_chat.server.*;
import info.u_team.voice_chat.server.VerifiedPlayerManager.PlayerData;
import net.minecraft.entity.player.ServerPlayerEntity;

public class VoiceToServerPacket {
	
	private final byte[] opusPacket;
	
	public VoiceToServerPacket(byte[] opusPacket) {
		this.opusPacket = opusPacket;
	}
	
	public static ByteBuffer encode(VoiceToServerPacket message) {
		return ByteBuffer.wrap(message.opusPacket);
	}
	
	public static VoiceToServerPacket decode(ByteBuffer buffer) {
		return new VoiceToServerPacket(buffer.array());
	}
	
	public static class Handler {
		
		public static void handle(VoiceToServerPacket message, Supplier<Context> contextSupplier) {
			final Context context = contextSupplier.get();
			if (!context.hasPlayer()) {
				return;
			}
			
			final ServerPlayerEntity player = context.getPlayer();
			
			final PlayerData playerData = VerifiedPlayerManager.getPlayerData(player);
			
			if (playerData == null) {
				return;
			}
			
			// TODO currently send the data to everybody even the sender
			VoiceServerManager.getServer().sendAllExcept(new VoiceToClientPacket(playerData.getId(), message.opusPacket), player);
			
			// VoiceServerManager.getServer().sendAll(new VoiceToClientPacket(playerData.getId(), message.opusPacket));
		}
	}
}
