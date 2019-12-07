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
	
	private final VoiceRecorder recorder;
	private final VoicePlayer player;
	
	private boolean handshakeDone;
	
	public VoiceClient(int port, byte[] secret) throws SocketException {
		this.secret = Arrays.copyOf(secret, secret.length);
		socket = new DatagramSocket();
		serverAddress = NetworkUtil.findServerInetAddress(port);
		receiveThread = new Thread(() -> receiveTask(), "Voice Client Receive");
		sendThread = new Thread(() -> sendTask(), "Voice Client Send");
		receiveThread.start();
		sendThread.start();
		recorder = new VoiceRecorder();
		player = new VoicePlayer();
	}
	
	public void close() {
		socket.close();
		recorder.close();
		player.close();
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
		try {
			while (!receiveThread.isInterrupted() && !socket.isClosed()) {
				receivePacket();
			}
		} catch (IOException ex) {
			if (!socket.isClosed()) {
				ex.printStackTrace();
			}
		}
	}
	
	private void sendTask() {
		try {
			// If handshake has not been done yet, send the packet every 500 ms
			while (!handshakeDone && !socket.isClosed()) {
				sendHandshakePacket();
				synchronized (this) {
					Thread.sleep(500);
				}
			}
			while (!sendThread.isInterrupted() && !socket.isClosed()) {
				sendPacket();
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
	
	private void sendPacket() throws IOException, InterruptedException {
		if (recorder.canSend()) {
			final byte[] opusPacket = recorder.getBytes();
			if (opusPacket.length > 1) {
				sendVoicePacket(opusPacket);
			}
		} else {
			synchronized (this) {
				Thread.sleep(200);
			}
		}
	}
	
	private void sendHandshakePacket() throws IOException {
		// Build packet
		byte[] data = new byte[9];
		data[0] = 0; // Handshake byte
		System.arraycopy(secret, 0, data, 1, secret.length);
		
		final DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress);
		socket.send(packet);
	}
	
	private void sendVoicePacket(byte[] opusPacket) throws IOException {
		// Build packet
		byte[] data = new byte[opusPacket.length + 9];
		data[0] = 1; // Voice packet byte
		System.arraycopy(secret, 0, data, 1, secret.length);
		System.arraycopy(opusPacket, 0, data, 9, opusPacket.length);
		
		final DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress);
		socket.send(packet);
	}
	
	private void receivePacket() throws IOException {
		final DatagramPacket packet = new DatagramPacket(new byte[1500], 1500);
		socket.receive(packet);
		// TODO logic with from what player etc
		handleVoicePacket(Arrays.copyOf(packet.getData(), packet.getLength()));
	}
	
	private void handleVoicePacket(byte[] packet) {
		player.play(packet);
	}
	
}
