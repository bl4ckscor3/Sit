package bl4ckscor3.mod.sit;

import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SitClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		EntityRendererRegistry.INSTANCE.register(Sit.SIT_ENTITY_TYPE, EmptyRenderer::new);
		ClientLoginNetworking.registerGlobalReceiver(Sit.VERSION_CHECK, this::onServerRequest);
	}

	private CompletableFuture<PacketByteBuf> onServerRequest(MinecraftClient minecraft, ClientLoginNetworkHandler listener, PacketByteBuf inBuf, Consumer<GenericFutureListener<? extends Future<? super Void>>> consumer)
	{
		//VERSION_CHECK request received from server, send back own version
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

		buf.writeInt(Sit.getModVersion());
		return CompletableFuture.completedFuture(buf);
	}

	private static class EmptyRenderer extends EntityRenderer<SitEntity>
	{
		protected EmptyRenderer(EntityRendererFactory.Context ctx)
		{
			super(ctx);
		}

		@Override
		public boolean shouldRender(SitEntity entity, Frustum frustum, double d, double e, double f)
		{
			return false;
		}

		@Override
		public Identifier getTexture(SitEntity entity)
		{
			return null;
		}
	}
}
