package info.u_team.voice_chat.audio_client.opus;

import java.nio.*;

import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.opus.Opus;

import info.u_team.voice_chat.audio_client.api.opus.IOpusEncoder;

public class PcmOpusEncoder implements IOpusEncoder {
	
	private final int frameSize;
	
	private final long instance;
	
	private final ByteBuffer inputBuffer;
	private final ShortBuffer inputShortBuffer;
	private final ByteBuffer outputBuffer;
	
	public PcmOpusEncoder(int sampleRate, int channel, int milliseconds, int bitrate, int signal, int bufferSize) {
		frameSize = sampleRate / (1000 / milliseconds);
		
		instance = Opus.opus_encoder_create(sampleRate, channel, Opus.OPUS_APPLICATION_AUDIO, null);
		Opus.opus_encoder_ctl(instance, Opus.OPUS_SET_BITRATE(bitrate));
		Opus.opus_encoder_ctl(instance, Opus.OPUS_SET_SIGNAL(signal));
		Opus.opus_encoder_ctl(instance, Opus.OPUS_SET_COMPLEXITY(10));
		
		inputBuffer = MemoryUtil.memCalloc(frameSize * channel * 2);
		inputBuffer.order(ByteOrder.LITTLE_ENDIAN);
		inputShortBuffer = inputBuffer.asShortBuffer();
		outputBuffer = MemoryUtil.memCalloc(bufferSize);
		
		inputBuffer.mark();
		inputShortBuffer.mark();
		outputBuffer.mark();
	}
	
	@Override
	public byte[] encode(byte[] pcm) {
		inputBuffer.reset();
		inputShortBuffer.reset();
		outputBuffer.reset();
		
		inputBuffer.put(pcm);
		
		final int encodedLength = Opus.opus_encode(instance, inputShortBuffer, frameSize, outputBuffer);
		
		final byte[] buffer = new byte[encodedLength];
		outputBuffer.get(buffer);
		return buffer;
	}
	
	@Override
	public byte[] silence() {
		return new byte[] { -8, -1, -2 };
	}
	
	@Override
	public void close() {
		Opus.opus_encoder_destroy(instance);
		MemoryUtil.memFree(inputBuffer);
		MemoryUtil.memFree(outputBuffer);
	}
	
}
