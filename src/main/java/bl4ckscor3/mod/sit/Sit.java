package bl4ckscor3.mod.sit;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(Sit.MODID)
@EventBusSubscriber(bus = Bus.MOD)
public class Sit {
	public static final String MODID = "sit";
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
	//@formatter:off
	public static final RegistryObject<EntityType<SitEntity>> SIT_ENTITY_TYPE = ENTITY_TYPES.register("entity_sit", () -> EntityType.Builder.<SitEntity>of(SitEntity::new, MobCategory.MISC)
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
