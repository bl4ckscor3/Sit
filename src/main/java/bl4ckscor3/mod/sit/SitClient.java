package bl4ckscor3.mod.sit;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod.EventBusSubscriber;
import net.neoforged.fml.common.Mod.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(bus = Bus.MOD, value = Dist.CLIENT)
public class SitClient {
	@SubscribeEvent
	public static void onFMLCLientSetup(FMLClientSetupEvent event) {
		EntityRenderers.register(Sit.SIT_ENTITY_TYPE.get(), EmptyRenderer::new);
	}

	private static class EmptyRenderer extends EntityRenderer<SitEntity> {
		protected EmptyRenderer(EntityRendererProvider.Context ctx) {
			super(ctx);
		}

		@Override
		public boolean shouldRender(SitEntity entity, Frustum camera, double camX, double camY, double camZ) {
			return false;
		}

		@Override
		public ResourceLocation getTextureLocation(SitEntity entity) {
			return null;
		}
	}
}
