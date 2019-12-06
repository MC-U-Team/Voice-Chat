package info.u_team.voice_chat.client;

import javax.sound.sampled.AudioFormat;

import org.concentus.*;

public class VoiceInfo {
	
	protected static final AudioFormat FORMAT = new AudioFormat(48000, 16, 2, true, false);
	
	static {
		OpusEncoder encoder;
		try {
			encoder = new OpusEncoder(48000, 2, OpusApplication.OPUS_APPLICATION_AUDIO);
			encoder.setBitrate(96000);
			encoder.setSignalType(OpusSignal.OPUS_SIGNAL_MUSIC);
			encoder.setComplexity(10);
		} catch (OpusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
