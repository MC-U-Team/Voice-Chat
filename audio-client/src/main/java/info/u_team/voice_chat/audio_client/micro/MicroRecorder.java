package info.u_team.voice_chat.audio_client.micro;

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
	
	private volatile boolean send;
	
	public MicroRecorder(MicroData microData, Consumer<byte[]> opusPacketConsumer) {
		this.microData = microData;
		this.opusPacketConsumer = opusPacketConsumer;
		this.encoder = new PcmOpusEncoder(48000, 2, 20, 64000, Opus.OPUS_SIGNAL_VOICE, 1000);
	}
	
	public void start() {
		if (send || !microData.isAvailable()) {
			return;
		}
		send = true;
		executor.execute(() -> {
			final byte[] buffer = new byte[960 * 2 * 2];
			while (send && microData.isAvailable()) {
				opusPacketConsumer.accept(encoder.encode(microData.read(buffer)));
			}
			ThreadUtil.execute(5, 20, () -> opusPacketConsumer.accept(encoder.silence()));
		});
	}
	
	public void stop() {
		send = false;
	}
	
	public boolean isSending() {
		return send;
	}
	
	@Override
	public void close() {
		executor.shutdown();
	}
}
