package bl4ckscor3.mod.sit;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.registries.ObjectHolder;

@Mod(Sit.MODID)
@EventBusSubscriber(bus = Bus.MOD)
public class Sit {
	public static final String MODID = "sit";
	@ObjectHolder(MODID + ":entity_sit")
	public static final EntityType<SitEntity> SIT_ENTITY_TYPE = null;

	public Sit() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Configuration.CONFIG_SPEC);
	}

	@SubscribeEvent
	public static void registerEntity(RegistryEvent.Register<EntityType<?>> event) {
		//@formatter:off
		event.getRegistry().register(EntityType.Builder.<SitEntity>of(SitEntity::new, MobCategory.MISC)
				.setTrackingRange(256)
				.setUpdateInterval(20)
				.sized(0.0001F, 0.0001F)
				.build(MODID + ":entity_sit")
				.setRegistryName(new ResourceLocation(MODID, "entity_sit")));
		//@formatter:on
	}
}
