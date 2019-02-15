package bl4ckscor3.mod.sit;

import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.common.Mod;

@Mod(Sit.MODID)
public class Sit
{
	public static final String MODID = "sit";
	public static final EntityType<EntitySit> SIT_ENTITY_TYPE = EntityType.register(MODID + ":entity_sit", EntityType.Builder.create(EntitySit.class, EntitySit::new).tracker(256, 20, false));
}
