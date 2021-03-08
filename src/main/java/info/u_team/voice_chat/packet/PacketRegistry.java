package info.u_team.voice_chat.packet;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.*;

import org.apache.logging.log4j.*;

import com.google.common.collect.*;

import info.u_team.voice_chat.packet.PacketRegistry.Context.Sender;
import net.minecraft.entity.player.ServerPlayerEntity;

public class PacketRegistry {
	
	/**
	 * This is the max good put packet size
	 */
	public static final int MAX_PACKET_SIZE = 1000;
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private static final Map<Class<?>, Packet<?>> packetClasses = new HashMap<>();
	private static final BiMap<Byte, Packet<?>> packets = HashBiMap.create();
	
	public static <MSG> void register(int id, Class<MSG> packetClass, Function<MSG, ByteBuffer> encoder, Function<ByteBuffer, MSG> decoder, BiConsumer<MSG, Supplier<Context>> messageConsumer) {
		register((byte) id, packetClass, encoder, decoder, messageConsumer);
	}
	
	public static <MSG> void register(byte id, Class<MSG> packetClass, Function<MSG, ByteBuffer> encoder, Function<ByteBuffer, MSG> decoder, BiConsumer<MSG, Supplier<Context>> messageConsumer) {
		final Packet<MSG> packet = new Packet<MSG>(encoder, decoder, messageConsumer);
		packetClasses.put(packetClass, packet);
		packets.put(id, packet);
	}
	
	@SuppressWarnings("unchecked")
	public static <MSG> byte[] encode(MSG message) {
		final Packet<MSG> packet = (Packet<MSG>) packetClasses.get(message.getClass());
		if (packet == null) {
			LOGGER.error("The message %s is not registered and cannot be encoded.", message.toString());
			return null;
		}
		final ByteBuffer buffer = packet.encode(message);
		buffer.position(0);
		final byte[] array = new byte[buffer.capacity() + 1];
		
		array[0] = packets.inverse().getOrDefault(packet, (byte) -1);
		buffer.get(array, 1, array.length - 1);
		
		return array;
	}
	
	@SuppressWarnings("unchecked")
	public static <MSG> MSG decode(byte[] array, int length) {
		if (array.length == 0) {
			return null;
		}
		final Packet<MSG> packet = (Packet<MSG>) packets.get(array[0]);
		if (packet == null) {
			LOGGER.error("The message with the id %i is not registered and cannot be decoded.", array[0]);
			return null;
		}
		final ByteBuffer buffer = ByteBuffer.allocate(length - 1);
		buffer.put(array, 1, length - 1);
		buffer.position(0);
		return packet.decode(buffer);
	}
	
	public static <MSG> void handle(MSG message, Sender sender, InetSocketAddress address) {
		handle(message, sender, address, null);
	}
	
	@SuppressWarnings("unchecked")
	public static <MSG> void handle(MSG message, Sender sender, InetSocketAddress address, ServerPlayerEntity player) {
		final Packet<MSG> packet = (Packet<MSG>) packetClasses.get(message.getClass());
		if (packet == null) {
			LOGGER.error("The message %s is not registered and cannot be handled.", message.toString());
		}
		try {
			packet.getMessageConsumer().accept(message, () -> new Context(sender, address, player));
		} catch (final Exception ex) {
			LOGGER.warn("An exception occured while handling the message %s", message.toString(), ex);
		}
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
		
		private final Sender sender;
		private final InetSocketAddress address;
		private final ServerPlayerEntity player;
		
		public Context(Sender sender, InetSocketAddress address, ServerPlayerEntity player) {
			this.sender = sender;
			this.address = address;
			this.player = player;
		}
		
		public Sender getSender() {
			return sender;
		}
		
		public InetSocketAddress getAddress() {
			return address;
		}
		
		public boolean hasPlayer() {
			return player != null;
		}
		
		public ServerPlayerEntity getPlayer() {
			return player;
		}
		
		public static enum Sender {
			SERVER,
			PLAYER;
		}
	}
	
}
