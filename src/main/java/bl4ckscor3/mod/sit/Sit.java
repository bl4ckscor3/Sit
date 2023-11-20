package bl4ckscor3.mod.sit;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Sit.MODID)
public class Sit {
	public static final String MODID = "sit";
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);
	public static final DeferredHolder<EntityType<?>, EntityType<SitEntity>> SIT_ENTITY_TYPE = ENTITY_TYPES.register("entity_sit", () -> EntityType.Builder.<SitEntity>of(SitEntity::new, MobCategory.MISC)
	//@formatter:off
			.setTrackingRange(256)
			.setUpdateInterval(20)
			.sized(0.0001F, 0.0001F)
			.build(MODID + ":entity_sit"));
	//@formatter:on

	public Sit() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Configuration.CONFIG_SPEC);
		ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
}
