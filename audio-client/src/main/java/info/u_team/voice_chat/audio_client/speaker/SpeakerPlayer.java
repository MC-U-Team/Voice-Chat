package info.u_team.voice_chat.audio_client.speaker;

import java.util.*;
import java.util.concurrent.*;

import info.u_team.voice_chat.audio_client.api.NoExceptionCloseable;
import info.u_team.voice_chat.audio_client.util.ThreadUtil;

public class SpeakerPlayer implements NoExceptionCloseable {
	
	private final ExecutorService executor = Executors.newCachedThreadPool(ThreadUtil.createDaemonFactory("speaker player"));
	
	private final SpeakerData speakerData;
	
	private final Map<Integer, SpeakerBufferPusher> bufferMap;
	
	public SpeakerPlayer(SpeakerData speakerData) {
		this.speakerData = speakerData;
		bufferMap = new HashMap<>();
	}
	
	public void accept(int id, byte[] opusPacket) {
		if (speakerData.isAvailable(id)) {
			bufferMap.computeIfAbsent(id, $ -> new SpeakerBufferPusher(executor, id, speakerData)).decodeAndPushPacket(opusPacket);
		}
	}
	
	public void setVolume(int volume) {
		speakerData.setVolume(volume);
	}
	
	@Override
	public void close() {
		bufferMap.values().forEach(SpeakerBufferPusher::close);
		bufferMap.clear();
		executor.shutdown();
	}
}
