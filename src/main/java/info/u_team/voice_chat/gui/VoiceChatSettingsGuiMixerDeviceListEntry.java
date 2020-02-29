package info.u_team.voice_chat.gui;

import info.u_team.u_team_core.gui.elements.ScrollableListEntry;

class VoiceChatSettingsGuiMixerDeviceListEntry extends ScrollableListEntry<VoiceChatSettingsGuiMixerDeviceListEntry> {
	
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
	
	@SuppressWarnings({ "deprecation" })
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		list.setSelected(this);
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
}