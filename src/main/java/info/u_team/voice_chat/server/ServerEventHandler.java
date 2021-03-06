package info.u_team.voice_chat.server;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import info.u_team.voice_chat.VoiceChatMod;
import info.u_team.voice_chat.config.CommonConfig;
import info.u_team.voice_chat.init.VoiceChatNetworks;
import info.u_team.voice_chat.message.PlayerIDJoinMessage;
import info.u_team.voice_chat.message.PlayerIDMessage;
import info.u_team.voice_chat.message.ServerPortHandshakeMessage;
import info.u_team.voice_chat.server.VerifiedPlayerManager.PlayerData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.network.PacketDistributor;

@EventBusSubscriber(modid = VoiceChatMod.MODID, bus = Bus.FORGE)
public class ServerEventHandler {
	
	@SubscribeEvent
	public static void login(PlayerLoggedInEvent event) {
		if (!(event.getPlayer() instanceof ServerPlayerEntity)) {
			return;
		}
		final ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
		// Add player secret
		PlayerSecretManager.addPlayer(player);
		
		// Send packet with port and secret
		VoiceChatNetworks.NETWORK.send(PacketDistributor.PLAYER.with(() -> player), new ServerPortHandshakeMessage(CommonConfig.getInstance().portValue.get(), PlayerSecretManager.getSecretByPlayer(player)));
		
		// Send packet with all currently connected players and their id
		final Map<UUID, PlayerData> map = VerifiedPlayerManager.getMap();
		
		final UUID[] uuids = new UUID[map.size()];
		final short[] ids = new short[map.size()];
		int counter = 0;
		for (final Entry<UUID, PlayerData> entry : map.entrySet()) {
			uuids[counter] = entry.getKey();
			ids[counter] = entry.getValue().getId();
			counter++;
		}
		
		VoiceChatNetworks.NETWORK.send(PacketDistributor.PLAYER.with(() -> player), new PlayerIDJoinMessage(uuids, ids));
	}
	
	@SubscribeEvent
	public static void logout(PlayerLoggedOutEvent event) {
		if (!(event.getPlayer() instanceof ServerPlayerEntity)) {
			return;
		}
		final ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
		
		// Remove verified player
		VerifiedPlayerManager.removePlayer(player);
		// Remove player secret
		PlayerSecretManager.removePlayer(player);
		
		// Send message to all players that this client is not connected anymore (we don't care if this is send to the
		// disconnecting player)
		VoiceChatNetworks.NETWORK.send(PacketDistributor.ALL.noArg(), new PlayerIDMessage(true, player.getUniqueID(), (short) 0));
	}
	
	@SubscribeEvent
	public static void start(FMLServerStartingEvent event) {
		VoiceServerManager.start();
	}
	
	@SubscribeEvent
	public static void stop(FMLServerStoppingEvent event) {
		VoiceServerManager.stop();
	}
	
}
