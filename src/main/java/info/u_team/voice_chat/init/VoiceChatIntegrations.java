package info.u_team.voice_chat.init;

import java.util.*;

import info.u_team.voice_chat.VoiceChatMod;
import info.u_team.voice_chat.api.IIntegration;
import info.u_team.voice_chat.musicplayer_integration.MusicPlayerIntegration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLLoader;

@EventBusSubscriber(modid = VoiceChatMod.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class VoiceChatIntegrations {
	
	public static final List<IIntegration> INTEGRATIONS = new ArrayList<>();
	
	@SubscribeEvent
	public static void register(FMLClientSetupEvent event) {
		if (FMLLoader.getLoadingModList().getModFileById("musicplayer") != null) {
			INTEGRATIONS.add(new MusicPlayerIntegration());
		}
	}
	
}