package info.u_team.voice_chat.audio_client.opus;

import java.util.Arrays;

import org.concentus.*;

import info.u_team.voice_chat.audio_client.api.opus.IOpusDecoder;

public class PcmOpusDecoder implements IOpusDecoder {
	
	private final OpusDecoder decoder;
	
	private final int frameSize;
	
	private final byte[] buffer;
	
	public PcmOpusDecoder(int sampleRate, int channel, int milliseconds) {
		try {
			decoder = new OpusDecoder(sampleRate, channel);
		} catch (OpusException ex) {
			throw new RuntimeException(ex);
		}
		frameSize = sampleRate / (1000 / milliseconds);
		buffer = new byte[frameSize * channel * 2];
	}
	
	@Override
	public byte[] decoder(byte[] opus) {
		try {
			decoder.decode(opus, 0, opus.length, buffer, 0, frameSize, false);
			return Arrays.copyOf(buffer, buffer.length);
		} catch (OpusException ex) {
			return new byte[buffer.length];
		}
	}
	
}
