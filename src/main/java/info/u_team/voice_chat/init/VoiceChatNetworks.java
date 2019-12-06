package info.u_team.voice_chat.init;

import info.u_team.voice_chat.VoiceChatMod;
import info.u_team.voice_chat.message.*;
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
		NETWORK.registerMessage(0, ServerPortMessage.class, ServerPortMessage::encode, ServerPortMessage::decode, ServerPortMessage.Handler::handle);
		NETWORK.registerMessage(1, ReadyMessage.class, ReadyMessage::encode, ReadyMessage::decode, ReadyMessage.Handler::handle);
	}
	
}
