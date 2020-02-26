package info.u_team.voice_chat.musicplayer_integration;

import java.util.concurrent.*;

import org.lwjgl.util.opus.Opus;

import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.voice_chat.api.IIntegration;
import info.u_team.voice_chat.audio_client.api.opus.IOpusEncoder;
import info.u_team.voice_chat.audio_client.opus.PcmOpusEncoder;
import info.u_team.voice_chat.audio_client.util.*;
import info.u_team.voice_chat.client.*;
import info.u_team.voice_chat.musicplayer_integration.message.MusicToServerPacket;
import net.minecraft.client.Minecraft;

public class MusicPlayerIntegration implements IIntegration {
	
	private ExecutorService executor;
	
	private IOpusEncoder encoder;
	
	@Override
	public void start() {
		executor = Executors.newSingleThreadExecutor(ThreadUtil.createDaemonFactory("music player sender"));
		encoder = new PcmOpusEncoder(48000, 2, 20, 96000, Opus.OPUS_SIGNAL_MUSIC, 1000);
		
		MusicPlayerManager.getPlayer().setOutputConsumer((buffer, length) -> executor.execute(() -> {
			if (MusicPlayerManager.getPlayer().getVolume() == 0) {
				return;
			}
			EndianUtil.endianConverter(buffer, 4); // We need little endian for opus but get big endian
			if (VoiceClientManager.isRunning()) {
				VoiceClientManager.getClient().send(new MusicToServerPacket(encoder.encode(buffer)));
				TalkingManager.addOrUpdate(Minecraft.getInstance().player.getUniqueID());
			}
		}));
	}
	
	@Override
	public void stop() {
		MusicPlayerManager.getPlayer().setOutputConsumer(null);
		executor.shutdown();
		encoder.close();
	}
	
}
