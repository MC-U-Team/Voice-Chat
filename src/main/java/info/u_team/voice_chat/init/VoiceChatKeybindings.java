package info.u_team.voice_chat.init;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.*;

public class VoiceChatKeybindings {
	
	public static final KeyBinding PUSH_TALK = new KeyBinding("push_talk", KeyConflictContext.UNIVERSAL, KeyModifier.NONE, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_KP_1, "Voice Chat");
	
}
