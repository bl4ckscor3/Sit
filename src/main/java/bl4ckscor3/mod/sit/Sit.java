package bl4ckscor3.mod.sit;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ObjectHolder;

@Mod(Sit.MODID)
@EventBusSubscriber(bus=Bus.MOD)
public class Sit
{
	public static final String MODID = "sit";
	@ObjectHolder(MODID + ":entity_sit")
	public static final EntityType<EntitySit> SIT_ENTITY_TYPE = null;

	@SubscribeEvent
	public static void registerEntity(RegistryEvent.Register<EntityType<?>> event)
	{
		event.getRegistry().register(EntityType.Builder.<EntitySit>create(EntitySit::new, EntityClassification.MISC)
				.setCustomClientFactory((spawnEntity, world) -> SIT_ENTITY_TYPE.create(world))
				.setTrackingRange(256)
				.setUpdateInterval(20)
				.size(0.0001F, 0.0001F)
				.build(MODID + ":entity_sit")
				.setRegistryName(new ResourceLocation(MODID, "entity_sit")));
	}
}
