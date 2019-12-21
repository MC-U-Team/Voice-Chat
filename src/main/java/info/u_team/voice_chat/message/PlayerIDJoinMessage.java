package info.u_team.voice_chat.message;

import java.util.UUID;
import java.util.function.Supplier;

import info.u_team.voice_chat.client.*;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PlayerIDJoinMessage {
	
	private final UUID[] uuids;
	private final short[] ids;
	
	public PlayerIDJoinMessage(UUID[] uuids, short[] ids) {
		this.uuids = uuids;
		this.ids = ids;
	}
	
	public static void encode(PlayerIDJoinMessage message, PacketBuffer buffer) {
		buffer.writeShort(message.uuids.length);
		for (int index = 0; index < message.uuids.length; index++) {
			buffer.writeUniqueId(message.uuids[index]);
			buffer.writeShort(message.ids[index]);
		}
	}
	
	public static PlayerIDJoinMessage decode(PacketBuffer buffer) {
		final int size = buffer.readShort();
		final UUID[] uuids = new UUID[size];
		final short[] ids = new short[size];
		for (int index = 0; index < size; index++) {
			uuids[index] = buffer.readUniqueId();
			ids[index] = buffer.readShort();
		}
		return new PlayerIDJoinMessage(uuids, ids);
	}
	
	public static class Handler {
		
		public static void handle(PlayerIDJoinMessage message, Supplier<Context> contextSupplier) {
			final Context context = contextSupplier.get();
			VoiceClientManager.EXECUTOR.execute(() -> PlayerIDManager.addAllPlayer(message.uuids, message.ids));
			context.setPacketHandled(true);
		}
	}
	
}
