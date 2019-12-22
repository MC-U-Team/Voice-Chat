package info.u_team.voice_chat.test;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import info.u_team.voice_chat.audio.*;

public class TestAudio {
	
	public static void main(String[] args) {
		
		SpeakerHandler speaker = new SpeakerHandler();
		
		MicroHandler micro = new MicroHandler() {
			
			@Override
			protected void sendVoicePacket(byte[] opusPacket) {
				speaker.receiveVoicePacket(0, opusPacket);
			}
		};
		
		JFrame frame = new JFrame();
		frame.setSize(300, 300);
		frame.setVisible(true);
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			
			@Override
			public boolean dispatchKeyEvent(KeyEvent ke) {
				switch (ke.getID()) {
				case KeyEvent.KEY_PRESSED:
					if (ke.getKeyCode() == 96) {
						micro.start();
					}
					break;
				case KeyEvent.KEY_RELEASED:
					if (ke.getKeyCode() == 96) {
						micro.stop();
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
		
		micro.close();
		speaker.close();
		
	}
	
}
