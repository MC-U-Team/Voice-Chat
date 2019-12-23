package info.u_team.voice_chat.audio_client.speaker;

import java.util.Map;
import java.util.concurrent.*;

import javax.sound.sampled.*;

import info.u_team.voice_chat.audio_client.api.NoExceptionCloseable;
import info.u_team.voice_chat.audio_client.util.*;

public class SpeakerData implements NoExceptionCloseable {
	
	public static final AudioFormat FORMAT = new AudioFormat(48000, 16, 2, true, false);
	public static final DataLine.Info SPEAKER_INFO = new DataLine.Info(SourceDataLine.class, FORMAT);
	
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(ThreadUtil.createDaemonFactory("speaker data cleanup"));
	
	private Mixer mixer;
	
	private final Map<Integer, SpeakerLineInfo> sourceLines;
	
	private final ScheduledFuture<?> cleanupTask;
	
	public SpeakerData(String speakerName) {
		setMixer(speakerName);
		sourceLines = new ConcurrentHashMap<>();
		cleanupTask = executor.scheduleWithFixedDelay(() -> {
			final long currentTime = System.currentTimeMillis();
			sourceLines.forEach((id, lineInfo) -> {
				if (currentTime - lineInfo.getLastAccessed() > 30000) {
					closeLine(sourceLines.remove(id)); // Because the map is concurrent
				}
			});
		}, 10, 10, TimeUnit.SECONDS);
	}
	
	private boolean createLine(int id) {
		if (mixer != null) {
			try {
				final SourceDataLine line = (SourceDataLine) mixer.getLine(SPEAKER_INFO);
				line.open(FORMAT, 960 * 2 * 2 * 4);
				line.start();
				sourceLines.put(id, new SpeakerLineInfo(line));
				return true;
			} catch (LineUnavailableException ex) {
			}
		}
		return false;
	}
	
	private void closeLine(SpeakerLineInfo lineInfo) {
		final SourceDataLine line = lineInfo.getSourceDataLine();
		line.flush();
		line.stop();
		line.close();
	}
	
	public void setMixer(String name) {
		mixer = AudioUtil.findMixer(name, SPEAKER_INFO);
		sourceLines.values().forEach(this::closeLine);
		sourceLines.clear();
	}
	
	public boolean isAvailable(int id) {
		if (mixer != null) {
			final SpeakerLineInfo lineInfo = sourceLines.get(id);
			if (lineInfo != null) {
				return lineInfo.getSourceDataLine().isOpen();
			} else {
				return createLine(id);
			}
		}
		return false;
	}
	
	public void flush(int id) {
		if (isAvailable(id)) {
			sourceLines.get(id).getSourceDataLine().flush();
		}
	}
	
	public byte[] write(int id, byte[] array) {
		if (isAvailable(id)) {
			sourceLines.get(id).getSourceDataLine().write(array, 0, array.length);
		}
		return array;
	}
	
	public int freeBuffer(int id) {
		if (isAvailable(id)) {
			return sourceLines.get(id).getSourceDataLine().available();
		}
		return 0;
	}
	
	@Override
	public void close() {
		cleanupTask.cancel(false);
		executor.shutdown();
		sourceLines.values().forEach(this::closeLine);
		sourceLines.clear();
		if (mixer != null) {
			mixer.close();
		}
	}
	
}
