package info.u_team.voice_chat.init;

import info.u_team.voice_chat.VoiceChatMod;
import info.u_team.voice_chat.message.HandshakeDoneMessage;
import info.u_team.voice_chat.message.PlayerIDJoinMessage;
import info.u_team.voice_chat.message.PlayerIDMessage;
import info.u_team.voice_chat.message.ServerPortHandshakeMessage;
import info.u_team.voice_chat.packet.PacketRegistry;
import info.u_team.voice_chat.packet.message.HandshakePacket;
import info.u_team.voice_chat.packet.message.VoiceToClientPacket;
import info.u_team.voice_chat.packet.message.VoiceToServerPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@EventBusSubscriber(modid = VoiceChatMod.MODID, bus = Bus.MOD)
public class VoiceChatNetworks {
	
	public static final String PROTOCOL = "1.14.4-1";
	
	public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(new ResourceLocation(VoiceChatMod.MODID, "network"), () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals);
	
	@SubscribeEvent
	public static void register(FMLCommonSetupEvent event) {
		NETWORK.registerMessage(0, ServerPortHandshakeMessage.class, ServerPortHandshakeMessage::encode, ServerPortHandshakeMessage::decode, ServerPortHandshakeMessage.Handler::handle);
		
		NETWORK.registerMessage(1, HandshakeDoneMessage.class, HandshakeDoneMessage::encode, HandshakeDoneMessage::decode, HandshakeDoneMessage.Handler::handle);
		NETWORK.registerMessage(2, PlayerIDJoinMessage.class, PlayerIDJoinMessage::encode, PlayerIDJoinMessage::decode, PlayerIDJoinMessage.Handler::handle);
		NETWORK.registerMessage(3, PlayerIDMessage.class, PlayerIDMessage::encode, PlayerIDMessage::decode, PlayerIDMessage.Handler::handle);
		
		PacketRegistry.register(0, HandshakePacket.class, HandshakePacket::encode, HandshakePacket::decode, HandshakePacket.Handler::handle);
		
		PacketRegistry.register(1, VoiceToServerPacket.class, VoiceToServerPacket::encode, VoiceToServerPacket::decode, VoiceToServerPacket.Handler::handle);
		PacketRegistry.register(2, VoiceToClientPacket.class, VoiceToClientPacket::encode, VoiceToClientPacket::decode, VoiceToClientPacket.Handler::handle);
	}
}
