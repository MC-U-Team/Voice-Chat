package info.u_team.voice_chat.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;

public class ClientConfig {
	
	public static final ForgeConfigSpec CONFIG;
	private static final ClientConfig INSTANCE;
	
	static {
		Pair<ClientConfig, ForgeConfigSpec> pair = new Builder().configure(ClientConfig::new);
		CONFIG = pair.getRight();
		INSTANCE = pair.getLeft();
	}
	
	public static ClientConfig getInstance() {
		return INSTANCE;
	}
	
	public final ConfigValue<String> microphoneValue;
	public final ConfigValue<String> speakerValue;
	
	public final IntValue bitrateValue;
	
	private ClientConfig(Builder builder) {
		builder.comment("Voice settings").push("client");
		microphoneValue = builder.comment("Which microphone should be used. If empty or microphone not found the default one will be used. If no microphone is found the mod cannot record voice data.").define("microphone", "");
		speakerValue = builder.comment("Which speaker should be used. If empty or speaker not found the default one will be used. If no speaker is found the mod cannot play voice data.").define("speaker", "");
		bitrateValue = builder.comment("The bitrate your voice packets are send to the server. If packet loss occurs try to lower the bitrate.").defineInRange("bitrate", 64000, 8000, 128000);
		builder.pop();
	}
	
}
