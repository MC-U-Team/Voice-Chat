package info.u_team.voice_chat.message;

import java.util.function.Supplier;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent.Context;

/**
 * This message will be send from the server if a player connects to let the client know of the port. This packet also
 * contains a "secret" for the player to be able to send voice packets to the server. The client must send an udp packet
 * (need to be specified still) to the server at this port and verify it. If that packet is not received in 2 seconds
 * this packet will be send again. After 5 retries the connections fails and an error should be displayed at the client
 * and server.
 * 
 * @author HyCraftHD
 */
public class ServerPortMessage {
	
	private final int port;
	private final byte[] secret;
	
	public ServerPortMessage(int port, byte[] secret) {
		this.port = port;
		this.secret = secret;
	}
	
	public int getPort() {
		return port;
	}
	
	public static void encode(ServerPortMessage message, PacketBuffer buffer) {
		buffer.writeInt(message.port);
		buffer.writeByteArray(message.secret);
	}
	
	public static ServerPortMessage decode(PacketBuffer buffer) {
		return new ServerPortMessage(buffer.readInt(), buffer.readByteArray());
	}
	
	public static class Handler {
		
		public static void handle(ServerPortMessage message, Supplier<Context> contextSupplier) {
			final Context context = contextSupplier.get();
			context.setPacketHandled(true);
		}
	}
	
}
