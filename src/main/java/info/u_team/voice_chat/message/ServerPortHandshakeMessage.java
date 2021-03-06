package info.u_team.voice_chat.message;

import java.util.function.Supplier;

import info.u_team.voice_chat.api.IIntegration;
import info.u_team.voice_chat.audio.MicroManager;
import info.u_team.voice_chat.audio.SpeakerManager;
import info.u_team.voice_chat.client.TalkingManager;
import info.u_team.voice_chat.client.VoiceClientManager;
import info.u_team.voice_chat.init.VoiceChatIntegrations;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class ServerPortHandshakeMessage {
	
	private final int port;
	private final byte[] secret;
	
	public ServerPortHandshakeMessage(int port, byte[] secret) {
		this.port = port;
		this.secret = secret;
	}
	
	public static void encode(ServerPortHandshakeMessage message, PacketBuffer buffer) {
		buffer.writeInt(message.port);
		buffer.writeByteArray(message.secret);
	}
	
	public static ServerPortHandshakeMessage decode(PacketBuffer buffer) {
		return new ServerPortHandshakeMessage(buffer.readInt(), buffer.readByteArray());
	}
	
	public static class Handler {
		
		public static void handle(ServerPortHandshakeMessage message, Supplier<Context> contextSupplier) {
			final Context context = contextSupplier.get();
			System.out.println("GOT message for voice server port: " + message.port);
			VoiceClientManager.EXECUTOR.execute(() -> {
				System.out.println("Stopping all manager");
				if (VoiceClientManager.isRunning()) {
					VoiceClientManager.stop();
				}
				if (MicroManager.isRunning()) {
					MicroManager.stop();
				}
				if (SpeakerManager.isRunning()) {
					SpeakerManager.stop();
				}
				if (TalkingManager.isRunning()) {
					TalkingManager.stop();
				}
				System.out.println("Start voice client with handshake init");
				VoiceClientManager.start(message.port, message.secret);
				MicroManager.start();
				SpeakerManager.start();
				TalkingManager.start();
				VoiceChatIntegrations.INTEGRATIONS.forEach(IIntegration::start);
			});
			context.setPacketHandled(true);
		}
	}
	
}
