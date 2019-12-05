package info.u_team.voice_chat.client;

import java.net.DatagramSocket;

public class VoiceClient {
	
	private final DatagramSocket socket;
	private final byte[] secret;
	
	public VoiceClient(DatagramSocket socket, byte[] secret) {
		this.socket = socket;
		this.secret = secret;
	}
	
}
