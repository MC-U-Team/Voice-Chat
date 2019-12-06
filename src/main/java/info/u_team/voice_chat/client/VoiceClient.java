package info.u_team.voice_chat.client;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

import info.u_team.voice_chat.util.NetworkUtil;

public class VoiceClient {
	
	private final byte[] secret;
	private final DatagramSocket socket;
	
	private final InetSocketAddress serverAddress;
	
	private final Thread receiveThread;
	private final Thread sendThread;
	
	private boolean handshakeDone;
	
	public VoiceClient(int port, byte[] secret) throws SocketException {
		this.secret = Arrays.copyOf(secret, secret.length);
		socket = new DatagramSocket();
		serverAddress = NetworkUtil.findServerInetAddress(port);
		receiveThread = new Thread(() -> receiveTask(), "Voice Client Receive");
		sendThread = new Thread(() -> sendTask(), "Voice Client Send");
		receiveThread.start();
		sendThread.start();
	}
	
	public void close() {
		socket.close();
		receiveThread.interrupt();
		sendThread.interrupt();
		try {
			// Wait for both threads to be closed
			receiveThread.join();
			sendThread.join();
		} catch (InterruptedException ex) {
			// Should not happen. Who interrupts the main thread??
		}
	}
	
	private void receiveTask() {
		while (!receiveThread.isInterrupted()) {
			
		}
	}
	
	private void sendTask() {
		try {
			// If handshake has not been done yet, send the packet every 500 ms
			while (!handshakeDone) {
				sendHandshakePacket();
				synchronized (this) {
					wait(500);
				}
			}
			while (!sendThread.isInterrupted()) {
				
			}
		} catch (IOException ex) {
			if (!socket.isClosed()) {
				ex.printStackTrace();
			}
		} catch (InterruptedException ex) {
		}
	}
	
	public void setHandshakeDone() {
		handshakeDone = true;
	}
	
	private void sendHandshakePacket() throws IOException {
		// Build packet
		byte[] data = new byte[9];
		data[0] = 0;
		System.arraycopy(secret, 0, data, 1, secret.length);
		
		final DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress);
		socket.send(packet);
	}
	
}
