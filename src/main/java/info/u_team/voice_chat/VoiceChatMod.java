package info.u_team.voice_chat;

import info.u_team.voice_chat.config.CommonConfig;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;

@Mod(VoiceChatMod.MODID)
public class VoiceChatMod {
	
	public static final String MODID = "voicechat";
	
	public VoiceChatMod() {
		ModLoadingContext.get().registerConfig(Type.COMMON, CommonConfig.CONFIG);
	}
	
}
