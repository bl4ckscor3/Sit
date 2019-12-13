package bl4ckscor3.mod.sit;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.util.Identifier;

public class SitClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		EntityRendererRegistry.INSTANCE.register(Sit.SIT_ENTITY_TYPE, (entityRenderDispatcher, context) -> new EmptyRenderer(entityRenderDispatcher));
	}

	private static class EmptyRenderer extends EntityRenderer<SitEntity>
	{
		protected EmptyRenderer(EntityRenderDispatcher entityRenderDispatcher)
		{
			super(entityRenderDispatcher);
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
