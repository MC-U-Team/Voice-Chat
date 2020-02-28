package info.u_team.voice_chat.render;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import info.u_team.voice_chat.client.TalkingManager;
import net.minecraft.client.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.play.*;
import net.minecraft.client.renderer.*;

public class RenderOverlayVoiceChat {
	
	public static void draw() {
		final Minecraft minecraft = Minecraft.getInstance();
		final MainWindow window = minecraft.getMainWindow();
		final int width = window.getScaledWidth();
		final int height = window.getScaledHeight();
		
		final FontRenderer fontRenderer = minecraft.fontRenderer;
		
		final AtomicInteger counter = new AtomicInteger();
		TalkingManager.getTalkers().stream().map(RenderOverlayVoiceChat::getName).forEach(name -> {
//			Minecraft.getInstance().fontRenderer.drawString(name, 10, counter.getAndIncrement() * 15 + 30, 0xFFFFFF);
		});
	}
	
	public static void drawEntry(int x, int y) {
		
	}
	
	private static String getName(UUID uuid) {
		final ClientPlayNetHandler connection = Minecraft.getInstance().getConnection();
		if (connection != null) {
			final NetworkPlayerInfo info = connection.getPlayerInfo(uuid);
			if (info != null) {
				return info.getGameProfile().getName();
			}
		}
		return uuid.toString();
	}
	
	private static int renderString(FontRenderer fontRenderer, String text, float x, float y, int color, Matrix4f matrix, boolean dropShadow) {
		if (text == null) {
			return 0;
		} else {
			final IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
			final int i = fontRenderer.renderString(text, x, y, color, dropShadow, matrix, irendertypebuffer$impl, false, 0, 15728880);
			irendertypebuffer$impl.finish();
			return i;
		}
	}
}
