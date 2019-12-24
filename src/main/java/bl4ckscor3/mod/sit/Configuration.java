package bl4ckscor3.mod.sit;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid=Sit.MODID)
@EventBusSubscriber(modid=Sit.MODID)
public class Configuration
{
	@Name("block_reach_distance")
	@Comment({"The maximum distance in blocks that the player can be away from a block to still be able to sit on it.",
		"Do note, that this is limited by how far a player can access a block in vanilla Minecraft.",
		"If this is set to 0, the player has to stand on top of the block to be able to sit on it."
	})
	@RangeInt(min=0, max=1000)
	public static int blockReachDistance = 4;

	@SubscribeEvent
	public static void onConfigChanged(OnConfigChangedEvent event)
	{
		if(event.getModID().equals(Sit.MODID))
			ConfigManager.sync(Sit.MODID, Config.Type.INSTANCE);
	}
}
