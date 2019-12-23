package info.u_team.voice_chat.test;

import javax.sound.sampled.*;

import org.concentus.OpusSignal;

import info.u_team.voice_chat.audio_client.api.opus.*;
import info.u_team.voice_chat.audio_client.opus.*;

public class TestTwoMics {
	
	private static final AudioFormat FORMAT = new AudioFormat(48000, 16, 2, true, false);
	private static final DataLine.Info MIC_INFO = new DataLine.Info(TargetDataLine.class, FORMAT);
	private static final DataLine.Info SPEAKER_INFO = new DataLine.Info(SourceDataLine.class, FORMAT);
	
	private static final IOpusEncoder ENCODER1 = new PcmOpusEncoder(48000, 2, 20, 64000, 1000, OpusSignal.OPUS_SIGNAL_VOICE);
	private static final IOpusDecoder DECORDER1 = new PcmOpusDecoder(48000, 2, 20);
	
	private static final IOpusEncoder ENCODER2 = new PcmOpusEncoder(48000, 2, 20, 64000, 1000, OpusSignal.OPUS_SIGNAL_VOICE);
	private static final IOpusDecoder DECORDER2 = new PcmOpusDecoder(48000, 2, 20);
	
	public static void main(String[] args) throws LineUnavailableException {
		final TargetDataLine micro1 = findMicrophoneOrUseDefault("Mikrofon (Auna Mic CM 900)");
		openTargetLine(micro1);
		
		final TargetDataLine micro2 = findMicrophoneOrUseDefault("Mikrofon (USB PnP Sound Device)");
		openTargetLine(micro2);
		
		final Mixer speakerMixer = findMixer("", SPEAKER_INFO);
		
		final SourceDataLine speaker1 = (SourceDataLine) speakerMixer.getLine(SPEAKER_INFO);
		openSourceLine(speaker1);
		
		final SourceDataLine speaker2 = (SourceDataLine) speakerMixer.getLine(SPEAKER_INFO);
		openSourceLine(speaker2);
		
		final byte[] readBuffer1 = new byte[960 * 2 * 2];
		final byte[] readBuffer2 = new byte[960 * 2 * 2];
		
		byte[] writeBuffer1;
		byte[] writeBuffer2;
		
		while (true) {
			micro1.read(readBuffer1, 0, readBuffer1.length);
			micro2.read(readBuffer2, 0, readBuffer2.length);
			
			writeBuffer1 = DECORDER1.decoder(ENCODER1.encode(readBuffer1));
			writeBuffer2 = DECORDER2.decoder(ENCODER2.encode(readBuffer2));
			
			speaker1.write(writeBuffer1, 0, writeBuffer1.length);
			speaker2.write(writeBuffer2, 0, writeBuffer2.length);
		}
	}
	
	private static void openTargetLine(TargetDataLine line) {
		try {
			line.open(FORMAT, 960 * 2 * 2 * 4);
			line.start();
		} catch (LineUnavailableException ex) {
		}
	}
	
	private static void openSourceLine(SourceDataLine line) {
		try {
			line.open(FORMAT, 960 * 2 * 2 * 4);
			line.start();
		} catch (LineUnavailableException ex) {
		}
	}
	
	public static Mixer findMixer(String name, Line.Info lineInfo) {
		Mixer defaultMixer = null;
		for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
			final Mixer mixer = AudioSystem.getMixer(mixerInfo);
			if (mixer.isLineSupported(lineInfo)) {
				if (mixerInfo.getName().equals(name)) {
					return mixer;
				}
				if (defaultMixer == null) {
					defaultMixer = mixer;
				}
			}
		}
		return defaultMixer;
	}
	
	private static TargetDataLine findMicrophoneOrUseDefault(String name) {
		try {
			for (Mixer.Info info : AudioSystem.getMixerInfo()) {
				final Mixer mixer = AudioSystem.getMixer(info);
				if (mixer.isLineSupported(MIC_INFO) && info.getName().equals(name)) {
					return (TargetDataLine) mixer.getLine(MIC_INFO);
				}
			}
			return (TargetDataLine) AudioSystem.getLine(MIC_INFO);
		} catch (LineUnavailableException ex) {
			return null;
		}
	}
	
}
