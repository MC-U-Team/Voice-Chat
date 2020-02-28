package info.u_team.voice_chat.render;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import com.mojang.blaze3d.systems.RenderSystem;

import info.u_team.voice_chat.VoiceChatMod;
import info.u_team.voice_chat.client.TalkingManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.network.play.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.ResourceLocation;

public class RenderOverlayVoiceChat {
	
	private static ResourceLocation SPEAKING = new ResourceLocation(VoiceChatMod.MODID, "textures/gui/speaking.png");
	
	public static void draw() {
		final Minecraft minecraft = Minecraft.getInstance();
		final FontRenderer fontRenderer = minecraft.fontRenderer;
		
		final AtomicInteger counter = new AtomicInteger();
		TalkingManager.getTalkers().stream().map(RenderOverlayVoiceChat::getName).forEach(name -> {
			drawEntry(5, 5 + counter.getAndIncrement() * 15, minecraft, fontRenderer, name);
		});
	}
	
	public static void drawEntry(int x, int y, Minecraft minecraft, FontRenderer fontRenderer, String name) {
		minecraft.getTextureManager().bindTexture(SPEAKING);
		RenderSystem.color3f(0, 0, 0);
		AbstractGui.blit(x, y, 8, 8, 0, 0, 128, 128, 128, 128);
		renderString(fontRenderer, name, 15 + x * (1 / 0.75F), 1.5F + y * (1 / 0.75F), 0xFF0000, Matrix4f.makeScale(0.75F, 0.75F, 0), false);
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
