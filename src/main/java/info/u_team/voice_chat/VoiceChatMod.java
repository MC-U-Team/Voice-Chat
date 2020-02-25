package info.u_team.voice_chat;

import org.apache.logging.log4j.*;
import org.lwjgl.util.opus.Opus;

import info.u_team.voice_chat.config.*;
import info.u_team.voice_chat.dependency.DependencyManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;

@Mod(VoiceChatMod.MODID)
public class VoiceChatMod {
	
	public static final String MODID = "voicechat";
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	public VoiceChatMod() {
		ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.CONFIG);
		ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfig.CONFIG);
		DependencyManager.construct();
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			int a = Opus.OPUS_SET_PHASE_INVERSION_DISABLED_REQUEST;
			System.out.println("JAAAAAAAAAAAAAAAAAJAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
			System.out.println(Opus.class);
			System.out.println(a);
		});
	}
	
}
