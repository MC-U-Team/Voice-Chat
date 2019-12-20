package info.u_team.voice_chat.client.musicplayer;

import java.net.SocketException;

import info.u_team.music_player.musicplayer.MusicPlayerManager;
import info.u_team.voice_chat.client.*;

public class MusicPlayerVoiceClient extends VoiceClient {
	
	private final MusicPlayerRecorder musicPlayerRecorder;
	
	public MusicPlayerVoiceClient(int port, byte[] secret) throws SocketException {
		super(port, secret);
		musicPlayerRecorder = new MusicPlayerRecorder();
	}
	
	@Override
	public void close() {
		musicPlayerRecorder.close();
		super.close();
	}
	
	private class MusicPlayerRecorder extends VoiceInfo {
		
		public MusicPlayerRecorder() {
			MusicPlayerManager.getPlayer().setOutputConsumer((buffer, chunkSize) -> {
				// Just send it always now
				if (!socket.isClosed()) {
//					try {
//						EndianUtil.endianConverter(buffer, 4);
//						final int encodedLength = ENCODER.encode(buffer, 0, 960, buffer, 0, buffer.length);
//						sendOpusPacket(2, Arrays.copyOf(buffer, encodedLength));
//					} catch (IOException | OpusException ex) {
//						ex.printStackTrace();
//					}
				}
			});
		}
		
		private void close() {
			MusicPlayerManager.getPlayer().setOutputConsumer(null);
		}
		
	}
	
}
