package info.u_team.voice_chat.render;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import com.mojang.blaze3d.matrix.MatrixStack;

import info.u_team.u_team_core.util.GuiUtil;
import info.u_team.voice_chat.VoiceChatMod;
import info.u_team.voice_chat.client.TalkingManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.network.play.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;

public class RenderOverlayVoiceChat {
	
	private static ResourceLocation SPEAKING = new ResourceLocation(VoiceChatMod.MODID, "textures/gui/speaking.png");
	
	public static void draw() {
		final Minecraft minecraft = Minecraft.getInstance();
		final FontRenderer fontRenderer = minecraft.fontRenderer;
		final AtomicInteger counter = new AtomicInteger();
		TalkingManager.getTalkers().stream().map(RenderOverlayVoiceChat::getName).forEach(name -> {
			drawEntry(new MatrixStack(), 5, 5 + counter.getAndIncrement() * 8, minecraft, fontRenderer, name);
		});
	}
	
	public static void drawEntry(MatrixStack stack, int x, int y, Minecraft minecraft, FontRenderer fontRenderer, String name) {
		minecraft.getTextureManager().bindTexture(SPEAKING);
		GuiUtil.clearColor();
		AbstractGui.blit(stack, x, y, 8, 8, 0, 0, 128, 128, 128, 128);
		renderString(fontRenderer, name, 15 + x * (1 / 0.75F), 1.5F + y * (1 / 0.75F), 0xFFFF00, Matrix4f.makeScale(0.75F, 0.75F, 0), false);
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
