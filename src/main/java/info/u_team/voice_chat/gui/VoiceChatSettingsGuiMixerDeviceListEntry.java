package info.u_team.voice_chat.gui;

import info.u_team.music_player.gui.BetterScrollableListEntry;

class VoiceChatSettingsGuiMixerDeviceListEntry extends BetterScrollableListEntry<VoiceChatSettingsGuiMixerDeviceListEntry> {
	
	private final String mixerName;
	
	public VoiceChatSettingsGuiMixerDeviceListEntry(String mixerName) {
		this.mixerName = mixerName;
	}
	
	@Override
	public void render(int slotIndex, int entryY, int entryX, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float partialTicks) {
		minecraft.fontRenderer.drawString(mixerName, entryX + 5, entryY + 5, 0x0083FF);
	}
	
	public String getMixerName() {
		return mixerName;
	}
	
}