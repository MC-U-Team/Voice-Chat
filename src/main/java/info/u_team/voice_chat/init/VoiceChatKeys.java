package info.u_team.voice_chat.init;

import org.lwjgl.glfw.GLFW;

import info.u_team.u_team_core.util.registry.ClientRegistry;
import info.u_team.voice_chat.VoiceChatMod;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = VoiceChatMod.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class VoiceChatKeys {
	
	public static final KeyBinding PUSH_TALK = new KeyBinding("push_talk", KeyConflictContext.UNIVERSAL, KeyModifier.NONE, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_KP_1, "Voice Chat");
	
	@SubscribeEvent
	public static void register(FMLClientSetupEvent event) {
		ClientRegistry.registerKeybinding(PUSH_TALK);
	}
}
