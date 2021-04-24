package info.u_team.voice_chat.musicplayer_integration.message;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

import info.u_team.voice_chat.packet.PacketRegistry.Context;
import info.u_team.voice_chat.server.VerifiedPlayerManager;
import info.u_team.voice_chat.server.VerifiedPlayerManager.PlayerData;
import info.u_team.voice_chat.server.VoiceServerManager;
import net.minecraft.entity.player.ServerPlayerEntity;

public class MusicToServerPacket {
	
	private final byte[] opusPacket;
	
	public MusicToServerPacket(byte[] opusPacket) {
		this.opusPacket = opusPacket;
	}
	
	public static ByteBuffer encode(MusicToServerPacket message) {
		return ByteBuffer.wrap(message.opusPacket);
	}
	
	public static MusicToServerPacket decode(ByteBuffer buffer) {
		return new MusicToServerPacket(buffer.array());
	}
	
	public static class Handler {
		
		public static void handle(MusicToServerPacket message, Supplier<Context> contextSupplier) {
			final Context context = contextSupplier.get();
			if (!context.hasPlayer()) {
				return;
			}
			
			final ServerPlayerEntity player = context.getPlayer();
			
			final PlayerData playerData = VerifiedPlayerManager.getPlayerData(player);
			
			if (playerData == null) {
				return;
			}
			
			// TODO currently send the data to everybody
			VoiceServerManager.getServer().sendAllExcept(new MusicToClientPacket(playerData.getId(), message.opusPacket), player);
		}
	}
	
}
