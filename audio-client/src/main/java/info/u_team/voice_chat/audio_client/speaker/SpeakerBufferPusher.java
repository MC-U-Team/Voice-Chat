package info.u_team.voice_chat.audio_client.speaker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import info.u_team.voice_chat.audio_client.api.NoExceptionCloseable;
import info.u_team.voice_chat.audio_client.opus.PcmOpusDecoder;

public class SpeakerBufferPusher implements NoExceptionCloseable {
	
	private final SpeakerBuffer buffer;
	private final PcmOpusDecoder decoder;
	private final Future<?> future;
	
	public SpeakerBufferPusher(ExecutorService executor, int id, SpeakerData speakerData) {
		buffer = new SpeakerBuffer(10);
		decoder = new PcmOpusDecoder(48000, 2, 20, 1000);
		future = executor.submit(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				if (speakerData.isAvailable(id) && speakerData.freeBuffer(id) > 0) {
					speakerData.write(id, buffer.getNextPacket());
				}
			}
		});
	}
	
	public void decodeAndPushPacket(byte[] opusPacket) {
		pushPacket(decoder.decoder(opusPacket));
	}
	
	private void pushPacket(byte[] packet) {
		buffer.pushPacket(packet);
	}
	
	@Override
	public void close() {
		future.cancel(true);
		decoder.close();
	}
	
}
