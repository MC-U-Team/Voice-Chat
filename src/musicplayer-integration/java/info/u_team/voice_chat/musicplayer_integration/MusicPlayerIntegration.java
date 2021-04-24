package info.u_team.voice_chat.musicplayer_integration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.lwjgl.util.opus.Opus;

import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.voice_chat.api.IIntegration;
import info.u_team.voice_chat.audio_client.api.opus.IOpusEncoder;
import info.u_team.voice_chat.audio_client.opus.PcmOpusEncoder;
import info.u_team.voice_chat.audio_client.util.EndianUtil;
import info.u_team.voice_chat.audio_client.util.ThreadUtil;
import info.u_team.voice_chat.client.TalkingManager;
import info.u_team.voice_chat.client.VoiceClientManager;
import info.u_team.voice_chat.config.ClientConfig;
import info.u_team.voice_chat.musicplayer_integration.message.MusicToServerPacket;
import net.minecraft.client.Minecraft;

public class MusicPlayerIntegration implements IIntegration {
	
	private static MusicPlayerIntegration INSTANCE;
	
	public static MusicPlayerIntegration getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MusicPlayerIntegration();
		}
		return INSTANCE;
	}
	
	private ExecutorService executor;
	
	private IOpusEncoder encoder;
	
	private volatile boolean shouldStream;
	
	@Override
	public void start() {
		executor = Executors.newSingleThreadExecutor(ThreadUtil.createDaemonFactory("music player sender"));
		encoder = new PcmOpusEncoder(48000, 2, 20, ClientConfig.getInstance().musicBitrateValue.get(), Opus.OPUS_SIGNAL_MUSIC, 1000);
		shouldStream = false;
		MusicPlayerManager.getPlayer().setOutputConsumer((buffer, length) -> executor.execute(() -> {
			if (!shouldStream) {
				return;
			}
			if (MusicPlayerManager.getPlayer().getVolume() == 0) {
				return;
			}
			if (VoiceClientManager.isRunning()) {
				EndianUtil.endianConverter(buffer, 4); // We need little endian for opus but get big endian
				final byte[] encoded = encoder.encode(buffer);
				VoiceClientManager.getClient().send(new MusicToServerPacket(encoded));
				TalkingManager.addOrUpdate(Minecraft.getInstance().player.getUniqueID());
			}
		}));
	}
	
	@Override
	public void stop() {
		MusicPlayerManager.getPlayer().setOutputConsumer(null);
		if (executor != null) {
			executor.shutdown();
			executor = null;
		}
		if (encoder != null) {
			encoder.close();
			encoder = null;
		}
		shouldStream = false;
	}
	
	public void setShouldStream(boolean shouldStream) {
		this.shouldStream = shouldStream;
	}
	
	public boolean isShouldStream() {
		return shouldStream;
	}
}
