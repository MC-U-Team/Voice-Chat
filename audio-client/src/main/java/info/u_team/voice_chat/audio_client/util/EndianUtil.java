package info.u_team.voice_chat.audio_client.util;

public class EndianUtil {
	
	public static void endianConverter(byte[] buffer, int length) {
		if (buffer.length % length != 0 || length % 2 != 0) {
			throw new IllegalStateException();
		}
		for (int index = 0; index < buffer.length; index += length) {
			for (int endianIndex = 0; endianIndex < length / 2; endianIndex++) {
				final byte temp = buffer[index + endianIndex];
				buffer[index + endianIndex] = buffer[index + length - endianIndex - 1];
				buffer[index + length - endianIndex - 1] = temp;
			}
		}
	}
	
}
