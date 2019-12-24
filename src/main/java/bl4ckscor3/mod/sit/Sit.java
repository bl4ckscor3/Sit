package bl4ckscor3.mod.sit;

import java.util.Arrays;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod(modid=Sit.MODID, name=Sit.NAME, version=Sit.VERSION, acceptedMinecraftVersions="[" + Sit.MC_VERSION + "]")
public class Sit
{
	public static final String MODID = "sit";
	public static final String NAME = "Sit";
	public static final String VERSION = "v1.2.4";
	public static final String MC_VERSION = "1.12.2";
	@Instance(MODID)
	public Sit instance;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		ModMetadata meta = event.getModMetadata();

		meta.authorList = Arrays.asList(new String[]{"bl4ckscor3"});
		meta.autogenerated = false;
		meta.description = "Adds the ability to sit on slabs and stairs.";
		meta.modId = MODID;
		meta.name = NAME;
		meta.version = VERSION;
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(MODID, "entity_sit"), EntitySit.class, "entity_sit", 0, instance, 256, 20, false);
	}
}
