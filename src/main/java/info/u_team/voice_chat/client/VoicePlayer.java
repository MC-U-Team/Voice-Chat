package info.u_team.voice_chat.client;

import javax.sound.sampled.*;

import org.concentus.OpusException;

import info.u_team.voice_chat.config.ClientConfig;

public class VoicePlayer extends VoiceInfo {
	
	private static final DataLine.Info SPEAKER_INFO = new DataLine.Info(SourceDataLine.class, FORMAT);
	
	private SourceDataLine sourceLine;
	
	public VoicePlayer() {
		setSourceLine(findSpeakerOrUseDefault(ClientConfig.getInstance().speakerValue.get()));
	}
	
	public void play(byte[] opusPacket) {
		final byte[] data = new byte[960 * 2 * 2];
		try {
			DECODER.decode(opusPacket, 0, opusPacket.length, data, 0, 960, false);
			sourceLine.write(data, 0, data.length);
		} catch (OpusException ex) {
		}
	}
	
	public void setSourceLine(SourceDataLine newLine) {
		if (sourceLine != null) {
			sourceLine.close();
		}
		sourceLine = newLine;
		try {
			sourceLine.open(FORMAT);
			sourceLine.start();
		} catch (LineUnavailableException ex) {
			sourceLine = null;
		}
	}
	
	public void close() {
		sourceLine.close();
	}
	
	private SourceDataLine findSpeakerOrUseDefault(String name) {
		try {
			for (Mixer.Info info : AudioSystem.getMixerInfo()) {
				final Mixer mixer = AudioSystem.getMixer(info);
				if (mixer.isLineSupported(SPEAKER_INFO) && info.getName().equals(name)) {
					return (SourceDataLine) mixer.getLine(SPEAKER_INFO);
				}
			}
			return (SourceDataLine) AudioSystem.getLine(SPEAKER_INFO);
		} catch (LineUnavailableException ex) {
			return null;
		}
	}
	
}
