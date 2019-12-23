package info.u_team.voice_chat.test;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.sound.sampled.*;
import javax.swing.JFrame;

import info.u_team.voice_chat.audio.*;
import info.u_team.voice_chat.audio_client.util.AudioUtil;

public class TestAudio {
	
	private static final AudioFormat FORMAT = new AudioFormat(48000, 16, 2, true, false);
	private static final DataLine.Info MIC_INFO = new DataLine.Info(TargetDataLine.class, FORMAT);
	
	public static void main(String[] args) {
		
		AudioUtil.findAudioDevices(MIC_INFO).forEach(System.out::println);
		
		final SpeakerHandler speaker = new SpeakerHandler();
		
		final MicroHandler micro1 = new MicroHandler() {
			
			@Override
			protected void sendVoicePacket(byte[] opusPacket) {
				System.out.println("Send opus packet to server with size: " + opusPacket.length);
				speaker.receiveVoicePacket(0, opusPacket);
			}
		};
		
		final MicroHandler micro2 = new MicroHandler() {
			
			@Override
			protected void sendVoicePacket(byte[] opusPacket) {
				speaker.receiveVoicePacket(1, opusPacket);
			}
		};
		
		// Just my mics for testing
		micro1.setMicro("Mikrofon (Auna Mic CM 900)");
		micro2.setMicro("Mikrofon (USB PnP Sound Device)");
		
		final JFrame frame = new JFrame();
		frame.setSize(300, 300);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			
			@Override
			public boolean dispatchKeyEvent(KeyEvent ke) {
				switch (ke.getID()) {
				case KeyEvent.KEY_PRESSED:
					if (ke.getKeyCode() == 96) {
						micro1.start();
					}
					if (ke.getKeyCode() == 97) {
						micro2.start();
					}
					break;
				case KeyEvent.KEY_RELEASED:
					if (ke.getKeyCode() == 96) {
						micro1.stop();
					}
					if (ke.getKeyCode() == 97) {
						micro2.stop();
					}
					break;
				}
				return false;
			}
		});
		
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		micro1.close();
		micro2.close();
		speaker.close();
		
	}
	
}
