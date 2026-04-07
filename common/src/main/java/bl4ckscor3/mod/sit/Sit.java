package bl4ckscor3.mod.sit;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class Sit {
	public static final String MODID = "sit";
	private static final Identifier SIT_ENTITY_ID = id("entity_sit");
	public static final Supplier<EntityType<?>> SIT_ENTITY_TYPE = Suppliers.memoize(() -> EntityType.Builder.<SitEntity>of(SitEntity::new, MobCategory.MISC)
		.clientTrackingRange(256)
		.updateInterval(20)
		.sized(0.0001F, 0.0001F)
		.build(ResourceKey.create(Registries.ENTITY_TYPE, SIT_ENTITY_ID)));
	private static Platform platform;

	public synchronized static void initialize(Platform platform) {
		if (Sit.platform != null) {
			throw new IllegalArgumentException("Sit platform has already been initialized");
		}

		Sit.platform = platform;
		platform.register(Registries.ENTITY_TYPE, SIT_ENTITY_TYPE, SIT_ENTITY_ID.getPath());
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MODID, path);
	}

	public static Platform platform() {
		return platform;
	}
}
