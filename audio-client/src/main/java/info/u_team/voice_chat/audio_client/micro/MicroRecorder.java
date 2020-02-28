package info.u_team.voice_chat.audio_client.micro;

import java.nio.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

import org.lwjgl.util.opus.Opus;

import info.u_team.voice_chat.audio_client.api.NoExceptionCloseable;
import info.u_team.voice_chat.audio_client.api.opus.IOpusEncoder;
import info.u_team.voice_chat.audio_client.opus.PcmOpusEncoder;
import info.u_team.voice_chat.audio_client.util.ThreadUtil;

public class MicroRecorder implements NoExceptionCloseable {
	
	private final ExecutorService executor = Executors.newSingleThreadExecutor(ThreadUtil.createDaemonFactory("micro recorder"));
	
	private final MicroData microData;
	private final Consumer<byte[]> opusPacketConsumer;
	private final IOpusEncoder encoder;
	private double volume;
	
	private volatile boolean send;
	
	public MicroRecorder(MicroData microData, Consumer<byte[]> opusPacketConsumer) {
		this.microData = microData;
		this.opusPacketConsumer = opusPacketConsumer;
		this.encoder = new PcmOpusEncoder(48000, 2, 20, 64000, Opus.OPUS_SIGNAL_VOICE, 1000);
		volume = 1;
	}
	
	public void start() {
		if (send || !microData.isAvailable()) {
			return;
		}
		send = true;
		executor.execute(() -> {
			final byte[] buffer = new byte[960 * 2 * 2];
			while (send && microData.isAvailable()) {
				opusPacketConsumer.accept(encoder.encode(adjustVolume(microData.read(buffer), volume)));
			}
			ThreadUtil.execute(5, 20, () -> opusPacketConsumer.accept(encoder.silence()));
		});
	}
	
	private byte[] adjustVolume(byte[] pcm, double volume) {
		if (Math.abs(volume - 1) < 0.001) {
			return pcm;
		}
		final ShortBuffer shortBuffer = ByteBuffer.wrap(pcm).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
		for (int index = 0; index < shortBuffer.capacity(); index++) {
			shortBuffer.put(index, (short) (shortBuffer.get(index) * volume));
		}
		return pcm;
	}
	
	public void stop() {
		send = false;
		microData.flush();
	}
	
	public void setVolume(double volume) {
		this.volume = volume;
	}
	
	public boolean isSending() {
		return send;
	}
	
	@Override
	public void close() {
		executor.shutdown();
		encoder.close();
	}
}
