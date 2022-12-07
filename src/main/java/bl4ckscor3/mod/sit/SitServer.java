package bl4ckscor3.mod.sit;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;

public class SitServer implements DedicatedServerModInitializer {
	@Override
	public void onInitializeServer() {
		if (FabricLoader.getInstance().isModLoaded("cloth-config")) 	{
			ConfigHandler.register();

			if (ConfigHandler.versionCheckDisabled())
				return;
		}

		//version check
		ServerLoginNetworking.registerGlobalReceiver(Sit.VERSION_CHECK, this::onClientResponse);
		ServerLoginConnectionEvents.QUERY_START.register(this::onLoginStart);
	}

	/**
	 * On login start, send VERSION_CHECK request
	 */
	private void onLoginStart(ServerLoginPacketListenerImpl serverLoginPacketListener, MinecraftServer server, PacketSender sender, ServerLoginNetworking.LoginSynchronizer sync) {
		//request the client to send its sit version number
		sender.sendPacket(Sit.VERSION_CHECK, PacketByteBufs.empty());
	}

	/**
	 * Handle the VERSION_CHECK response
	 */
	private void onClientResponse(MinecraftServer server, ServerLoginPacketListenerImpl listener, boolean understood, FriendlyByteBuf buf, ServerLoginNetworking.LoginSynchronizer loginSynchronizer, PacketSender packetSender) {
		//client did not respond in time or doesn't use the correct version, disconnect client
		if (!understood || buf.readInt() != Sit.getModVersion())
			listener.disconnect(Sit.INCORRECT_VERSION);
	}
}