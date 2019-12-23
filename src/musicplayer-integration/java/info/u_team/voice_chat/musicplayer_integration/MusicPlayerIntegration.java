package info.u_team.voice_chat.musicplayer_integration;

import org.concentus.OpusSignal;

import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.voice_chat.api.IIntegration;
import info.u_team.voice_chat.audio_client.api.opus.IOpusEncoder;
import info.u_team.voice_chat.audio_client.opus.PcmOpusEncoder;
import info.u_team.voice_chat.audio_client.util.EndianUtil;
import info.u_team.voice_chat.client.*;
import info.u_team.voice_chat.musicplayer_integration.message.MusicToServerPacket;
import net.minecraft.client.Minecraft;

public class MusicPlayerIntegration implements IIntegration {
	
	private final IOpusEncoder encoder = new PcmOpusEncoder(48000, 2, 20, 96000, 1000, OpusSignal.OPUS_SIGNAL_MUSIC);
	
	@Override
	public void start() {
		MusicPlayerManager.getPlayer().setOutputConsumer((buffer, length) -> {
			if (MusicPlayerManager.getPlayer().getVolume() == 0) {
				return;
			}
			EndianUtil.endianConverter(buffer, 4); // We need little endian for opus but get big endian
			if (VoiceClientManager.isRunning()) {
				VoiceClientManager.getClient().send(new MusicToServerPacket(encoder.encode(buffer)));
				TalkingManager.addOrUpdate(Minecraft.getInstance().player.getUniqueID());
			}
		});
	}
	
	@Override
	public void stop() {
		MusicPlayerManager.getPlayer().setOutputConsumer(null);
	}
	
}
