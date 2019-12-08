package info.u_team.voice_chat.message;

import java.util.UUID;
import java.util.function.Supplier;

import info.u_team.voice_chat.client.PlayerIDList;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class PlayerIDMessage {
	
	private final boolean delete;
	private final UUID uuid;
	private final short id;
	
	public PlayerIDMessage(boolean delete, UUID uuid, short id) {
		this.delete = delete;
		this.uuid = uuid;
		this.id = id;
	}
	
	public static void encode(PlayerIDMessage message, PacketBuffer buffer) {
		buffer.writeBoolean(message.delete);
		buffer.writeUniqueId(message.uuid);
		if (!message.delete) {
			buffer.writeShort(message.id);
		}
	}
	
	public static PlayerIDMessage decode(PacketBuffer buffer) {
		final boolean delete = buffer.readBoolean();
		return new PlayerIDMessage(delete, buffer.readUniqueId(), delete ? 0 : buffer.readShort());
	}
	
	public static class Handler {
		
		public static void handle(PlayerIDMessage message, Supplier<Context> contextSupplier) {
			final Context context = contextSupplier.get();
			if (message.delete) {
				PlayerIDList.removePlayer(message.uuid);
			} else {
				PlayerIDList.addPlayer(message.uuid, message.id);
			}
			context.setPacketHandled(true);
		}
	}
	
}
