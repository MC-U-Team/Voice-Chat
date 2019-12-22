package info.u_team.voice_chat.packet.message;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

import info.u_team.voice_chat.audio.*;
import info.u_team.voice_chat.packet.PacketRegistry.Context;

public class VoiceToClientPacket {
	
	private final short id;
	private final byte[] opusPacket;
	
	public VoiceToClientPacket(short id, byte[] opusPacket) {
		this.id = id;
		this.opusPacket = opusPacket;
	}
	
	public static ByteBuffer encode(VoiceToClientPacket message) {
		final ByteBuffer buffer = ByteBuffer.allocate(2 + message.opusPacket.length);
		buffer.putShort(message.id);
		buffer.put(message.opusPacket);
		return buffer;
	}
	
	public static VoiceToClientPacket decode(ByteBuffer buffer) {
		final short id = buffer.getShort();
		final byte[] opusPacket = new byte[buffer.remaining()];
		buffer.get(opusPacket);
		return new VoiceToClientPacket(id, opusPacket);
	}
	
	public static class Handler {
		
		public static void handle(VoiceToClientPacket message, Supplier<Context> contextSupplier) {
			if (SpeakerManager.isRunning()) {
				SpeakerManager.getHandler().receiveVoicePacket(message.id, message.opusPacket);
			}
		}
	}
	
}
