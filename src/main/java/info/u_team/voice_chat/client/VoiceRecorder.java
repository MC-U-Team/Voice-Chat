package info.u_team.voice_chat.client;

import java.util.Arrays;

import javax.sound.sampled.*;

import org.concentus.OpusException;

import info.u_team.voice_chat.config.ClientConfig;
import info.u_team.voice_chat.init.VoiceChatKeybindings;

public class VoiceRecorder extends VoiceInfo {
	
	private static final DataLine.Info MIC_INFO = new DataLine.Info(TargetDataLine.class, FORMAT);
	
	private TargetDataLine targetLine;
	
	public VoiceRecorder() {
		setTargetLine(findMicrophoneOrUseDefault(ClientConfig.getInstance().microphoneValue.get()));
	}
	
	public boolean canSend() {
		return targetLine != null && targetLine.isOpen() && VoiceChatKeybindings.PUSH_TALK.isKeyDown();
	}
	
	public byte[] getBytes() {
		final byte[] data = new byte[960 * 2 * 2]; // Used for 20 ms audio frames with 2 channels at 48khz
		targetLine.read(data, 0, data.length);
		try {
			final int encodedLength = ENCODER.encode(data, 0, 960, data, 0, data.length);
			return Arrays.copyOf(data, encodedLength);
		} catch (OpusException ex) {
			return new byte[0];
		}
	}
	
	public void setTargetLine(TargetDataLine newLine) {
		if (targetLine != null) {
			targetLine.close();
		}
		targetLine = newLine;
		try {
			targetLine.open(FORMAT);
			targetLine.start();
		} catch (LineUnavailableException ex) {
			targetLine = null;
		}
	}
	
	public void close() {
		targetLine.close();
	}
	
	private TargetDataLine findMicrophoneOrUseDefault(String name) {
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
