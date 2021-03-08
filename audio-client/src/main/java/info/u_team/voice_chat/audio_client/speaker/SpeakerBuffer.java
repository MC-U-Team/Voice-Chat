package info.u_team.voice_chat.audio_client.speaker;

import java.util.concurrent.*;

public class SpeakerBuffer {
	
	private final BlockingQueue<byte[]> queue;
	
	public SpeakerBuffer(int size) {
		queue = new LinkedBlockingQueue<>(size);
	}
	
	public byte[] getNextPacket() {
		try {
			return queue.take();
		} catch (final InterruptedException ex) {
			throw new AssertionError();
		}
	}
	
	public void pushPacket(byte[] packet) {
		if (!queue.offer(packet)) {
			queue.poll();
			queue.offer(packet);
		}
	}
	
}
