package info.u_team.voice_chat.audio_client.micro;

import java.util.concurrent.*;
import java.util.function.Consumer;

import info.u_team.voice_chat.audio_client.api.ResourceClosable;
import info.u_team.voice_chat.audio_client.api.opus.IOpusEncoder;
import info.u_team.voice_chat.audio_client.util.Util;

public class MicroRecorder implements ResourceClosable {
	
	public static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor(Util.createDaemonFactory("micro recorder"));
	
	private final MicroData microData;
	private final Consumer<byte[]> opusPacketConsumer;
	private final IOpusEncoder encoder;
	
	private volatile boolean send;
	
	public MicroRecorder(MicroData microData, Consumer<byte[]> opusPacketConsumer, IOpusEncoder encoder) {
		this.microData = microData;
		this.opusPacketConsumer = opusPacketConsumer;
		this.encoder = encoder;
	}
	
	public void start() {
		if (send || !microData.isAvailable()) {
			return;
		}
		send = true;
		EXECUTOR.execute(() -> {
			final byte[] buffer = new byte[960 * 2 * 2];
			while (send && microData.isAvailable()) {
				opusPacketConsumer.accept(encoder.encode(microData.read(buffer)));
			}
			Util.execute(5, 20, () -> opusPacketConsumer.accept(encoder.silence()));
		});
	}
	
	public void stop() {
		send = false;
	}
	
	@Override
	public void close() {
		EXECUTOR.shutdown();
	}
}
