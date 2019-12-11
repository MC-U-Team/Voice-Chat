package info.u_team.voice_chat.util;

public enum PacketType {
	
	UNKNOWN(-1),
	HANDSHAKE(0),
	VOICE(1),
	MUSIC(2);
	
	private final byte id;
	
	private PacketType(int id) {
		this.id = (byte) id;
	}
	
	public byte getID() {
		return id;
	}
	
	public boolean isUnknown() {
		return this == UNKNOWN;
	}
	
	public boolean isHandshake() {
		return this == HANDSHAKE;
	}
	
	public boolean isOpus() {
		return this == VOICE || this == MUSIC;
	}
	
	public static PacketType byID(int id) {
		switch (id) {
		case 0:
			return HANDSHAKE;
		case 1:
			return VOICE;
		case 2:
			return MUSIC;
		default:
			return UNKNOWN;
		}
	}
	
}
