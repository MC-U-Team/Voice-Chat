package info.u_team.voice_chat.audio_client.opus;

import java.util.Arrays;

import org.concentus.*;

import info.u_team.voice_chat.audio_client.api.opus.IOpusEncoder;

public class PcmOpusEncoder implements IOpusEncoder {
	
	private final OpusEncoder encoder;
	
	private final int frameSize;
	
	private final byte[] buffer;
	
	public PcmOpusEncoder(int sampleRate, int channel, int milliseconds, int bitrate, int bufferSize, OpusSignal signal) {
		try {
			encoder = new OpusEncoder(sampleRate, channel, OpusApplication.OPUS_APPLICATION_AUDIO);
			encoder.setBitrate(bitrate);
			encoder.setSignalType(signal);
			encoder.setComplexity(10);
		} catch (OpusException ex) {
			throw new RuntimeException(ex);
		}
		frameSize = sampleRate / (1000 / milliseconds);
		buffer = new byte[bufferSize];
	}
	
	@Override
	public byte[] encode(byte[] pcm) {
		try {
			final int encodedLength = encoder.encode(pcm, 0, frameSize, buffer, 0, buffer.length);
			return Arrays.copyOf(buffer, encodedLength);
		} catch (OpusException ex) {
			return silence();
		}
	}
	
	@Override
	public byte[] silence() {
		return new byte[] { -8, -1, -2 };
	}
	
}
