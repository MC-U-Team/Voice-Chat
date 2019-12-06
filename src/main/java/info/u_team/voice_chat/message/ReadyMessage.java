package info.u_team.voice_chat.message;

import java.util.function.Supplier;

import info.u_team.voice_chat.client.VoiceClientManager;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class ReadyMessage {
	
	public static void encode(ReadyMessage message, PacketBuffer buffer) {
	}
	
	public static ReadyMessage decode(PacketBuffer buffer) {
		return new ReadyMessage();
	}
	
	public static class Handler {
		
		public static void handle(ReadyMessage message, Supplier<Context> contextSupplier) {
			final Context context = contextSupplier.get();
			VoiceClientManager.setHandshakeDone();
			context.setPacketHandled(true);
		}
	}
	
}
