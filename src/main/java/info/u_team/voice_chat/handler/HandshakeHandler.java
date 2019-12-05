package info.u_team.voice_chat.handler;

import info.u_team.voice_chat.VoiceChatMod;
import info.u_team.voice_chat.config.CommonConfig;
import info.u_team.voice_chat.init.VoiceChatNetworks;
import info.u_team.voice_chat.message.ServerPortMessage;
import info.u_team.voice_chat.server.PlayerSecretList;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.network.PacketDistributor;

@EventBusSubscriber(modid = VoiceChatMod.MODID, bus = Bus.FORGE)
public class HandshakeHandler {
	
	@SubscribeEvent
	public static void login(PlayerLoggedInEvent event) {
		if (!(event.getPlayer() instanceof ServerPlayerEntity)) {
			return;
		}
		final ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
		// Add player secret
		PlayerSecretList.addPlayer(player);
		
		// Send packet with port and secret
		VoiceChatNetworks.NETWORK.send(PacketDistributor.PLAYER.with(() -> player), new ServerPortMessage(CommonConfig.getInstance().portValue.get(), PlayerSecretList.getSecretByPlayer(player)));
	}
	
	@SubscribeEvent
	public static void login(PlayerLoggedOutEvent event) {
		if (!(event.getPlayer() instanceof ServerPlayerEntity)) {
			return;
		}
		final ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
		// Remove player secret
		PlayerSecretList.removePlayer(player);
	}
	
}
