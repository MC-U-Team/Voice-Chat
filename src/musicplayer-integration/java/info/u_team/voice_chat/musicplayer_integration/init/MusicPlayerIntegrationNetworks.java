package info.u_team.voice_chat.musicplayer_integration.init;

import info.u_team.voice_chat.VoiceChatMod;
import info.u_team.voice_chat.musicplayer_integration.message.*;
import info.u_team.voice_chat.packet.PacketRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = VoiceChatMod.MODID, bus = Bus.MOD)
public class MusicPlayerIntegrationNetworks {
	
	@SubscribeEvent
	public static void register(FMLCommonSetupEvent event) {
		PacketRegistry.register(3, MusicToServerPacket.class, MusicToServerPacket::encode, MusicToServerPacket::decode, MusicToServerPacket.Handler::handle);
		PacketRegistry.register(4, MusicToClientPacket.class, MusicToClientPacket::encode, MusicToClientPacket::decode, MusicToClientPacket.Handler::handle);
	}
	
}
