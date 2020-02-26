package info.u_team.voice_chat.audio_client.opus;

import java.nio.*;

import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.opus.Opus;

import info.u_team.voice_chat.audio_client.api.opus.IOpusDecoder;

public class PcmOpusDecoder implements IOpusDecoder {
	
	private final int frameSize;
	
	private final long instance;
	
	private final ByteBuffer inputBuffer;
	private final ByteBuffer outputBuffer;
	private final ShortBuffer outputShortBuffer;
	
	public PcmOpusDecoder(int sampleRate, int channel, int milliseconds, int bufferSize) {
		frameSize = sampleRate / (1000 / milliseconds);
		
		instance = Opus.opus_decoder_create(sampleRate, channel, null);
		
		inputBuffer = MemoryUtil.memCalloc(bufferSize);
		outputBuffer = MemoryUtil.memCalloc(frameSize * channel * 2);
		outputBuffer.order(ByteOrder.LITTLE_ENDIAN);
		outputShortBuffer = outputBuffer.asShortBuffer();
		
		inputBuffer.mark();
		outputBuffer.mark();
		outputShortBuffer.mark();
	}
	
	@Override
	public byte[] decoder(byte[] opus) {
		inputBuffer.reset();
		outputBuffer.reset();
		outputShortBuffer.reset();
		
		inputBuffer.limit(opus.length);
		inputBuffer.put(opus);
		inputBuffer.reset();
		
		Opus.opus_decode(instance, inputBuffer, outputShortBuffer, frameSize, 0);
		
		final byte[] buffer = new byte[outputBuffer.capacity()];
		outputBuffer.get(buffer);
		return buffer;
	}
	
	@Override
	public void close() {
		Opus.opus_decoder_destroy(instance);
		MemoryUtil.memFree(inputBuffer);
		MemoryUtil.memFree(outputBuffer);
	}
	
}
