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
		EntityRendererRegistry.INSTANCE.register(Sit.ENTITY_SIT, (entityRenderDispatcher, context) -> new EmptyRenderer(entityRenderDispatcher));
	}

	private static class EmptyRenderer extends EntityRenderer<EntitySit>
	{
		protected EmptyRenderer(EntityRenderDispatcher entityRenderDispatcher)
		{
			super(entityRenderDispatcher);
		}

		@Override
		public boolean shouldRender(EntitySit entity, Frustum frustum, double d, double e, double f)
		{
			return false;
		}

		@Override
		public Identifier getTexture(EntitySit entity)
		{
			return null;
		}
	}
}
