package info.u_team.voice_chat.gui;

import info.u_team.u_team_core.gui.elements.ScrollableListEntry;

public abstract class BetterScrollableListEntry<T extends ScrollableListEntry<T>> extends ScrollableListEntry<T> {
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		list.setSelected((T) this);
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
}
