package info.u_team.voice_chat.client;

import java.util.Arrays;

import javax.sound.sampled.*;

import org.concentus.OpusException;

import info.u_team.voice_chat.config.ClientConfig;
import info.u_team.voice_chat.init.VoiceChatKeybindings;

public class VoiceRecorder extends VoiceInfo {
	
	private static final DataLine.Info MIC_INFO = new DataLine.Info(TargetDataLine.class, FORMAT);
	
	private TargetDataLine targetLine;
	
	private boolean keyPressed;
	
	private int stoppedPressing = 0;
	
	public VoiceRecorder() {
		setTargetLine(findMicrophoneOrUseDefault(ClientConfig.getInstance().microphoneValue.get()));
	}
	
	public boolean canSend() {
		if (targetLine == null || !targetLine.isOpen()) {
			return false;
		}
		final boolean currentState = VoiceChatKeybindings.PUSH_TALK.isKeyDown();
		if (stoppedPressing > 0) {
			keyPressed = currentState;
			return true;
		} else if (keyPressed && !currentState) {
			stoppedPressing = 1;
			keyPressed = currentState;
			return true;
		} else {
			return keyPressed = currentState;
		}
	}
	
	public byte[] getBytes() {
		if (stoppedPressing > 0) {
			stoppedPressing++;
			if (stoppedPressing > 6) {
				if (stoppedPressing >= 8) {
					stoppedPressing = 0;
				}
				return new byte[] { 0, 0 }; // Packet so we know that we stop sending (send more for safety)
			}
			return new byte[] { -8, -1, -2 }; // Silent opus packet (f8fffe)
		} else {
			final byte[] data = new byte[960 * 2 * 2]; // Used for 20 ms audio frames with 2 channels at 48khz
			targetLine.read(data, 0, data.length);
			try {
				final int encodedLength = ENCODER.encode(data, 0, 960, data, 0, data.length);
				return Arrays.copyOf(data, encodedLength);
			} catch (OpusException ex) {
				return new byte[0];
			}
		}
	}
	
	public void setTargetLine(TargetDataLine newLine) {
		if (targetLine != null) {
			targetLine.close();
		}
		targetLine = newLine;
		try {
			targetLine.open(FORMAT, 960 * 2 * 2 * 2);
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
