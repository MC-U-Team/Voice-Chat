package info.u_team.voice_chat.musicplayer_integration.util;

import java.util.function.*;

import info.u_team.music_player.musicplayer.MusicPlayerManager;
import net.minecraftforge.fml.ModList;

public class MusicPlayerIntegrationUtil {
	
	private static Boolean INSTALLED;
	
	public static boolean isMusicPlayerInstalled() {
		if (INSTALLED == null) {
			INSTALLED = ModList.get().isLoaded("musicplayer");
		}
		return INSTALLED;
	}
	
	public static boolean isShowIngameMenueOverlay() {
		if (isMusicPlayerInstalled()) {
			final Supplier<BooleanSupplier> supplier = () -> () -> MusicPlayerManager.getSettingsManager().getSettings().isShowIngameMenueOverlay(); // Use the supplier method to prevent loading the classes
			return supplier.get().getAsBoolean();
		} else {
			return false;
		}
	}
	
}
