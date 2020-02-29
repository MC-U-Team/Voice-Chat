package info.u_team.voice_chat.audio;

import info.u_team.voice_chat.audio_client.api.NoExceptionCloseable;
import info.u_team.voice_chat.audio_client.micro.*;
import info.u_team.voice_chat.client.*;
import info.u_team.voice_chat.config.ClientConfig;
import info.u_team.voice_chat.packet.message.VoiceToServerPacket;
import net.minecraft.client.Minecraft;

public class MicroHandler implements NoExceptionCloseable {
	
	private final ClientConfig config;
	
	private final MicroData data;
	private final MicroRecorder recorder;
	
	public MicroHandler() {
		config = ClientConfig.getInstance();
		data = new MicroData(config.microphoneValue.get(), config.microphoneVolumeValue.get());
		recorder = new MicroRecorder(data, this::sendVoicePacket, config.bitrateValue.get());
	}
	
	protected void sendVoicePacket(byte[] opusPacket) {
		if (VoiceClientManager.isRunning()) {
			VoiceClientManager.getClient().send(new VoiceToServerPacket(opusPacket));
			TalkingManager.addOrUpdate(Minecraft.getInstance().player.getUniqueID());
		}
	}
	
	public void start() {
		recorder.start();
	}
	
	public void stop() {
		recorder.stop();
	}
	
	public boolean isSending() {
		return recorder.isSending();
	}
	
	public String getMicro() {
		return data.getMixer();
	}
	
	public void setMicro(String mixer) {
		data.setMixer(mixer);
		config.microphoneValue.set(mixer);
		ClientConfig.CONFIG.save();
	}
	
	public int getVolume() {
		return data.getVolume();
	}
	
	public void setVolume(int volume) {
		data.setVolume(volume);
		config.microphoneVolumeValue.set(volume);
		ClientConfig.CONFIG.save();
	}
	
	@Override
	public void close() {
		recorder.close();
		data.close();
	}
	
}
