package info.u_team.voice_chat.audio;

import info.u_team.voice_chat.audio_client.api.NoExceptionCloseable;
import info.u_team.voice_chat.audio_client.micro.*;
import info.u_team.voice_chat.client.VoiceClientManager;
import info.u_team.voice_chat.packet.message.VoiceToServerPacket;

public class MicroHandler implements NoExceptionCloseable {
	
	private final MicroData data;
	private final MicroRecorder recorder;
	
	public MicroHandler() {
		data = new MicroData("");
		recorder = new MicroRecorder(data, this::sendVoicePacket);
	}
	
	protected void sendVoicePacket(byte[] opusPacket) {
		if (VoiceClientManager.isRunning()) {
			System.out.println("Send opus packet to server with size: " + opusPacket.length);
			VoiceClientManager.getClient().send(new VoiceToServerPacket(opusPacket));
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
	
	public void setMicro(String mixer) {
		data.setMixer(mixer);
	}
	
	@Override
	public void close() {
		recorder.close();
		data.close();
	}
	
}
