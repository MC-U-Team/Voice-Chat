package info.u_team.voice_chat.audio_client.speaker;

import java.util.concurrent.*;

import info.u_team.voice_chat.audio_client.api.opus.IOpusDecoder;
import info.u_team.voice_chat.audio_client.util.Util;

public class SpeakerPlayer {
	
	public static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor(Util.createDaemonFactory("speaker player"));
	
	private final SpeakerData speakerData;
	private final IOpusDecoder decoder;
	private final SpeakerBuffer buffer;
	
	private volatile boolean play;
	
	public SpeakerPlayer(SpeakerData speakerData, IOpusDecoder decoder) {
		this.speakerData = speakerData;
		this.decoder = decoder;
		buffer = new SpeakerBuffer(10);
	}
	
	public void accept(byte[] opusPacket) {
		if (speakerData.isAvailable()) {
			buffer.pushPacket(decoder.decoder(opusPacket));
		}
	}
	
	public void start() {
		play = true;
		EXECUTOR.execute(() -> {
			while (play) {
				if (speakerData.isAvailable() && speakerData.freeBuffer() > 0) {
					speakerData.write(buffer.getNextPacket());
				}
			}
		});
	}
	
	public void close() {
		play = false;
		EXECUTOR.shutdown();
	}
}
