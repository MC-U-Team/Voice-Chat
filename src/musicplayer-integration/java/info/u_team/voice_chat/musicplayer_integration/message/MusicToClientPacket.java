package info.u_team.voice_chat.musicplayer_integration.message;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

import info.u_team.voice_chat.audio.SpeakerManager;
import info.u_team.voice_chat.client.*;
import info.u_team.voice_chat.packet.PacketRegistry.Context;

public class MusicToClientPacket {
	
	private final short id;
	private final byte[] opusPacket;
	
	public MusicToClientPacket(short id, byte[] opusPacket) {
		this.id = id;
		this.opusPacket = opusPacket;
	}
	
	public static ByteBuffer encode(MusicToClientPacket message) {
		final ByteBuffer buffer = ByteBuffer.allocate(2 + message.opusPacket.length);
		buffer.putShort(message.id);
		buffer.put(message.opusPacket);
		return buffer;
	}
	
	public static MusicToClientPacket decode(ByteBuffer buffer) {
		final short id = buffer.getShort();
		final byte[] opusPacket = new byte[buffer.remaining()];
		buffer.get(opusPacket);
		return new MusicToClientPacket(id, opusPacket);
	}
	
	public static class Handler {
		
		public static void handle(MusicToClientPacket message, Supplier<Context> contextSupplier) {
			if (SpeakerManager.isRunning()) {
				SpeakerManager.getHandler().receiveVoicePacket(message.id + 100000, message.opusPacket);
				TalkingManager.addOrUpdate(PlayerIDManager.getPlayerByID(message.id));
			}
		}
	}
	
}
