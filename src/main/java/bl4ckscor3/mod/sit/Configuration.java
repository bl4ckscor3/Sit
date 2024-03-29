package bl4ckscor3.mod.sit;

import org.apache.commons.lang3.tuple.Pair;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

public class Configuration {
	public static final ModConfigSpec CONFIG_SPEC;
	public static final Configuration CONFIG;
	public final IntValue blockReachDistance;

	static {
		Pair<Configuration, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Configuration::new);

		CONFIG_SPEC = specPair.getRight();
		CONFIG = specPair.getLeft();
	}

	Configuration(ModConfigSpec.Builder builder) {
		//@formatter:off
		blockReachDistance = builder
				.comment("The maximum distance in blocks that the player can be away from a block to still be able to sit on it.",
						"Do note, that this is limited by how far a player can access a block in vanilla Minecraft.",
						"If this is set to 0, the player has to stand on top of the block to be able to sit on it.")
				.defineInRange("block_reach_distance", 4, 0, 1000);
		//@formatter:on
	}
}
