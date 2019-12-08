package info.u_team.voice_chat.client;

import javax.sound.sampled.AudioFormat;

import org.concentus.*;

import info.u_team.voice_chat.config.ClientConfig;

public class VoiceInfo {
	
	protected static final AudioFormat FORMAT = new AudioFormat(48000, 16, 2, true, false);
	
	protected static final OpusEncoder ENCODER;
	protected static final OpusDecoder DECODER;
	
	static {
		try {
			ENCODER = new OpusEncoder(48000, 2, OpusApplication.OPUS_APPLICATION_AUDIO);
			ENCODER.setBitrate(ClientConfig.getInstance().bitrateValue.get());
			ENCODER.setSignalType(OpusSignal.OPUS_SIGNAL_AUTO);
			ENCODER.setComplexity(10);
			
			DECODER = new OpusDecoder(48000, 2);
		} catch (OpusException ex) {
			throw new RuntimeException(ex); // This should never happen
		}
	}
}
