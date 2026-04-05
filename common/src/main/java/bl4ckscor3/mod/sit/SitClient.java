package bl4ckscor3.mod.sit;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class SitClient {
	public static void registerRenderer(EntityRegistration registrar) {
		registrar.register(Sit.SIT_ENTITY_TYPE.get(), NoopRenderer::new);
	}

	@FunctionalInterface
	public interface EntityRegistration {
		<T extends Entity> void register(EntityType<? extends T> entityType, EntityRendererProvider<T> entityRendererProvider);
	}
}
