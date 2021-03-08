package info.u_team.voice_chat.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;

public class ClientConfig {
	
	public static final ForgeConfigSpec CONFIG;
	private static final ClientConfig INSTANCE;
	
	static {
		final Pair<ClientConfig, ForgeConfigSpec> pair = new Builder().configure(ClientConfig::new);
		CONFIG = pair.getRight();
		INSTANCE = pair.getLeft();
	}
	
	public static ClientConfig getInstance() {
		return INSTANCE;
	}
	
	public final ConfigValue<String> microphoneValue;
	public final IntValue microphoneVolumeValue;
	
	public final ConfigValue<String> speakerValue;
	public final IntValue speakerVolumeValue;
	
	public final IntValue bitrateValue;
	public final IntValue musicBitrateValue;
	
	private ClientConfig(Builder builder) {
		builder.comment("Voice settings").push("client");
		builder.push("microphone");
		microphoneValue = builder.comment("Which microphone should be used. If empty or microphone not found the default one will be used. If no microphone is found the mod cannot record voice data.").define("microphone", "");
		microphoneVolumeValue = builder.comment("The volume of the microphone in a linear range").defineInRange("volume", 100, 0, 150);
		builder.pop();
		
		builder.push("speaker");
		speakerValue = builder.comment("Which speaker should be used. If empty or speaker not found the default one will be used. If no speaker is found the mod cannot play voice data.").define("speaker", "");
		speakerVolumeValue = builder.comment("The volume of the speaker in a linear range").defineInRange("volume", 100, 0, 150);
		builder.pop();
		
		bitrateValue = builder.comment("The bitrate your voice packets are send to the server. If packet loss occurs try to lower the bitrate.").defineInRange("bitrate", 64000, 8000, 128000);
		musicBitrateValue = builder.comment("The bitrate your music packets are send to the server if your stream your music player. If packet loss occurs try to lower the bitrate.").defineInRange("bitrate", 96000, 8000, 128000);
		builder.pop();
	}
	
}
