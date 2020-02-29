package info.u_team.voice_chat.config;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;

public class CommonConfig {
	
	public static final ForgeConfigSpec CONFIG;
	private static final CommonConfig INSTANCE;
	
	static {
		Pair<CommonConfig, ForgeConfigSpec> pair = new Builder().configure(CommonConfig::new);
		CONFIG = pair.getRight();
		INSTANCE = pair.getLeft();
	}
	
	public static CommonConfig getInstance() {
		return INSTANCE;
	}
	
	public final IntValue portValue;
	
	private CommonConfig(Builder builder) {
		builder.comment("Voice Server configuration settings").push("server");
		portValue = builder.comment("The udp port the voice chat server is listening on. This port with the ip the client connects to must be exposed in the firewall!").defineInRange("port", 25566, 0, 65535);
		builder.pop();
	}
	
}
