package info.u_team.voice_chat.audio_client.speaker;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

import info.u_team.voice_chat.audio_client.api.NoExceptionCloseable;
import info.u_team.voice_chat.audio_client.util.AudioUtil;
import info.u_team.voice_chat.audio_client.util.ThreadUtil;

public class SpeakerData implements NoExceptionCloseable {
	
	public static final AudioFormat FORMAT = new AudioFormat(48000, 16, 2, true, false);
	public static final DataLine.Info SPEAKER_INFO = new DataLine.Info(SourceDataLine.class, FORMAT);
	
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(ThreadUtil.createDaemonFactory("speaker data cleanup"));
	
	private Mixer mixer;
	
	private final Map<Integer, SpeakerLineInfo> sourceLines;
	
	private int volume;
	
	private final ScheduledFuture<?> cleanupTask;
	
	public SpeakerData(String speakerName, int volume) {
		sourceLines = new ConcurrentHashMap<>();
		setMixer(speakerName);
		setVolume(volume);
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
				final SpeakerLineInfo lineInfo = new SpeakerLineInfo(line);
				lineInfo.setMasterVolume(volume);
				sourceLines.put(id, lineInfo);
				return true;
			} catch (final LineUnavailableException ex) {
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
	
	public String getMixer() {
		if (mixer != null) {
			return mixer.getMixerInfo().getName();
		}
		return null;
	}
	
	public void setMixer(String name) {
		if (mixer != null && mixer.getMixerInfo().getName().equals(name)) {
			return;
		}
		final Mixer oldMixer = mixer;
		mixer = AudioUtil.findMixer(name, SPEAKER_INFO);
		sourceLines.values().forEach(this::closeLine);
		sourceLines.clear();
		if (oldMixer != null) {
			if (!AudioUtil.hasLinesOpen(oldMixer)) {
				oldMixer.close();
			}
		}
	}
	
	public int getVolume() {
		return volume;
	}
	
	public void setVolume(int volume) {
		this.volume = volume;
		sourceLines.values().stream().forEach(lineInfo -> lineInfo.setMasterVolume(volume));
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
			final SpeakerLineInfo lineInfo = sourceLines.get(id);
			if (!lineInfo.isMasterVolumeControlFound()) {
				AudioUtil.changeVolume(array, volume, lineInfo.getMultiplier());
			}
			lineInfo.getSourceDataLine().write(array, 0, array.length);
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
			if (!AudioUtil.hasLinesOpen(mixer)) {
				mixer.close();
			}
		}
	}
	
}
