package info.u_team.voice_chat;

import org.apache.logging.log4j.*;

import info.u_team.u_team_core.util.verify.JarSignVerifier;
import info.u_team.voice_chat.config.*;
import info.u_team.voice_chat.dependency.DependencyManager;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;

@Mod(VoiceChatMod.MODID)
public class VoiceChatMod {
	
	public static final String MODID = "voicechat";
	
	public static final Logger LOGGER = LogManager.getLogger();
	
	public VoiceChatMod() {
		JarSignVerifier.checkSigned(MODID);
		ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.CONFIG);
		ModLoadingContext.get().registerConfig(Type.CLIENT, ClientConfig.CONFIG);
		DependencyManager.construct();
	}
	
}
