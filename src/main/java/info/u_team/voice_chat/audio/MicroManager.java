package info.u_team.voice_chat.audio;

import org.concentus.OpusSignal;

import info.u_team.voice_chat.audio_client.api.opus.IOpusEncoder;
import info.u_team.voice_chat.audio_client.micro.*;
import info.u_team.voice_chat.audio_client.opus.PcmOpusEncoder;

public class MicroManager {
	
	private static final IOpusEncoder encoder = new PcmOpusEncoder(48000, 2, 20, 64000, 1000, OpusSignal.OPUS_SIGNAL_VOICE);
	
	private final MicroData data;
	private final MicroRecorder recorder;
	
	public MicroManager() {
		data = new MicroData("");
		recorder = new MicroRecorder(data, this::handeVoicePacket, encoder);
	}
	
	private void handeVoicePacket(byte[] opusPacket) {
	}
	
	public void start() {
		recorder.start();
	}
	
	public void stop() {
		recorder.stop();
	}
	
	public void close() {
		data.close();
		recorder.close();
	}
	
}
