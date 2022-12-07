package bl4ckscor3.mod.sit;

import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SitClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(Sit.SIT_ENTITY_TYPE, EmptyRenderer::new);
		ClientLoginNetworking.registerGlobalReceiver(Sit.VERSION_CHECK, this::onServerRequest);
	}

	private CompletableFuture<FriendlyByteBuf> onServerRequest(Minecraft minecraft, ClientHandshakePacketListenerImpl listener, FriendlyByteBuf inBuf, Consumer<GenericFutureListener<? extends Future<? super Void>>> consumer)  {
		//VERSION_CHECK request received from server, send back own version
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

		buf.writeInt(Sit.getModVersion());
		return CompletableFuture.completedFuture(buf);
	}

	private static class EmptyRenderer extends EntityRenderer<SitEntity> {
		protected EmptyRenderer(EntityRendererProvider.Context ctx) {
			super(ctx);
		}

		@Override
		public boolean shouldRender(SitEntity entity, Frustum frustum, double d, double e, double f) {
			return false;
		}

		@Override
		public ResourceLocation getTextureLocation(SitEntity entity) {
			return null;
		}
	}
}