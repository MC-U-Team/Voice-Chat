package info.u_team.voice_chat.packet;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.*;

import org.apache.logging.log4j.*;

import com.google.common.collect.*;

import info.u_team.voice_chat.packet.PacketRegistry.Context.Sender;
import net.minecraft.entity.player.PlayerEntity;

public class PacketRegistry {
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final Map<Class<?>, Packet<?>> packetClasses = new HashMap<>();
	private static final BiMap<Byte, Packet<?>> packets = HashBiMap.create();
	
	public static <MSG> void register(byte id, Class<MSG> packetClass, Function<MSG, ByteBuffer> encoder, Function<ByteBuffer, MSG> decoder, BiConsumer<MSG, Supplier<Context>> messageConsumer) {
		final Packet<MSG> packet = new Packet<MSG>(encoder, decoder, messageConsumer);
		packetClasses.put(packetClass, packet);
		packets.put(id, packet);
	}
	
	@SuppressWarnings("unchecked")
	public static <MSG> byte[] encode(MSG message) {
		final Packet<MSG> packet = (Packet<MSG>) packetClasses.get(message);
		if (packet == null) {
			LOGGER.error("The message %s is not registered and cannot be encoded.", message);
			return null;
		}
		final ByteBuffer buffer = packet.encode(message);
		final byte[] array = new byte[buffer.capacity() + 1];
		
		array[0] = packets.inverse().getOrDefault(packet, (byte) -1);
		buffer.get(array, 1, array.length);
		
		return array;
	}
	
	@SuppressWarnings("unchecked")
	public static <MSG> MSG decode(byte[] array) {
		if (array.length == 0) {
			return null;
		}
		final Packet<MSG> packet = (Packet<MSG>) packets.get(array[0]);
		if (packet == null) {
			LOGGER.error("The message with the id %s is not registered and cannot be decoded.", array[0]);
			return null;
		}
		return packet.decode(ByteBuffer.wrap(array, 1, array.length));
	}
	
	public static <MSG> void handle(MSG message, Sender sender) {
		handle(message, sender, null);
	}
	
	@SuppressWarnings("unchecked")
	public static <MSG> void handle(MSG message, Sender sender, PlayerEntity player) {
		final Packet<MSG> packet = (Packet<MSG>) packetClasses.get(message);
		if (packet == null) {
			LOGGER.error("The message %s is not registered and cannot be handled.", message);
		}
		packet.getMessageConsumer().accept(message, () -> new Context(sender, player));
	}
	
	private static class Packet<MSG> {
		
		private final Function<MSG, ByteBuffer> encoder;
		private final Function<ByteBuffer, MSG> decoder;
		private final BiConsumer<MSG, Supplier<Context>> messageConsumer;
		
		public Packet(Function<MSG, ByteBuffer> encoder, Function<ByteBuffer, MSG> decoder, BiConsumer<MSG, Supplier<Context>> messageConsumer) {
			this.encoder = encoder;
			this.decoder = decoder;
			this.messageConsumer = messageConsumer;
		}
		
		public ByteBuffer encode(MSG message) {
			return encoder.apply(message);
		}
		
		public MSG decode(ByteBuffer array) {
			return decoder.apply(array);
		}
		
		public BiConsumer<MSG, Supplier<Context>> getMessageConsumer() {
			return messageConsumer;
		}
		
	}
	
	public static class Context {
		
		private Sender sender;
		private final PlayerEntity player;
		
		public Context(Sender sender, PlayerEntity player) {
			this.sender = sender;
			this.player = player;
		}
		
		public Sender getSender() {
			return sender;
		}
		
		public boolean hasPlayer() {
			return player != null;
		}
		
		public PlayerEntity getPlayer() {
			return player;
		}
		
		public static enum Sender {
			SERVER,
			PLAYER;
		}
	}
	
}
