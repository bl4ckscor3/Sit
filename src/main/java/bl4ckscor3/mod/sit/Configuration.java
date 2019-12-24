package bl4ckscor3.mod.sit;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class Configuration
{
	public static final ForgeConfigSpec CONFIG_SPEC;
	public static final Configuration CONFIG;

	public final IntValue blockReachDistance;

	static
	{
		Pair<Configuration,ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Configuration::new);

		CONFIG_SPEC = specPair.getRight();
		CONFIG = specPair.getLeft();
	}

	Configuration(ForgeConfigSpec.Builder builder)
	{
		blockReachDistance = builder
				.comment("The maximum distance in blocks that the player can be away from a block to still be able to sit on it.",
						"Do note, that this is limited by how far a player can access a block in vanilla Minecraft.",
						"If this is set to 0, the player has to stand on top of the block to be able to sit on it.")
				.defineInRange("block_reach_distance", 4, 0, 1000);
	}
}
