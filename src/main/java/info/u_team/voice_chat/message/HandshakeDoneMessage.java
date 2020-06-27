package info.u_team.voice_chat.message;

import java.util.function.Supplier;

import info.u_team.voice_chat.client.VoiceClientManager;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class HandshakeDoneMessage {
	
	public static void encode(HandshakeDoneMessage message, PacketBuffer buffer) {
	}
	
	public static HandshakeDoneMessage decode(PacketBuffer buffer) {
		return new HandshakeDoneMessage();
	}
	
	public static class Handler {
		
		public static void handle(HandshakeDoneMessage message, Supplier<Context> contextSupplier) {
			final Context context = contextSupplier.get();
			VoiceClientManager.setHandshakeDone();
			context.setPacketHandled(true);
		}
	}
	
}
