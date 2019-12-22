package info.u_team.voice_chat.audio;

import org.concentus.OpusSignal;

import info.u_team.voice_chat.audio_client.api.NoExceptionCloseable;
import info.u_team.voice_chat.audio_client.api.opus.IOpusEncoder;
import info.u_team.voice_chat.audio_client.micro.*;
import info.u_team.voice_chat.audio_client.opus.PcmOpusEncoder;
import info.u_team.voice_chat.client.VoiceClientManager;
import info.u_team.voice_chat.packet.message.VoiceToServerPacket;

public class MicroHandler implements NoExceptionCloseable {
	
	private static final IOpusEncoder encoder = new PcmOpusEncoder(48000, 2, 20, 64000, 1000, OpusSignal.OPUS_SIGNAL_VOICE);
	
	private final MicroData data;
	private final MicroRecorder recorder;
	
	public MicroHandler() {
		data = new MicroData("");
		recorder = new MicroRecorder(data, this::sendVoicePacket, encoder);
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
