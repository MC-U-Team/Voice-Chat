package info.u_team.voice_chat.audio_client.speaker;

import java.util.concurrent.*;

import info.u_team.voice_chat.audio_client.api.NoExceptionCloseable;

public class SpeakerBufferPusher implements NoExceptionCloseable {
	
	private final SpeakerBuffer buffer;
	private final Future<?> future;
	
	public SpeakerBufferPusher(ExecutorService executor, int id, SpeakerData speakerData) {
		buffer = new SpeakerBuffer(10);
		future = executor.submit(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				if (speakerData.isAvailable(id) && speakerData.freeBuffer(id) > 0) {
					speakerData.write(id, buffer.getNextPacket());
				}
			}
		});
	}
	
	public void pushPacket(byte[] packet) {
		buffer.pushPacket(packet);
	}
	
	@Override
	public void close() {
		future.cancel(true);
	}
	
}
