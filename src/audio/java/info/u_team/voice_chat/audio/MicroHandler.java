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
			VoiceClientManager.getClient().send(new VoiceToServerPacket(opusPacket));
		}
	}
	
	public void start() {
		recorder.start();
	}
	
	public void stop() {
		recorder.stop();
	}
	
	public void setMicro(String micro) {
		data.setTargetLine(micro);
	}
	
	@Override
	public void close() {
		recorder.close();
		data.close();
	}
	
}
