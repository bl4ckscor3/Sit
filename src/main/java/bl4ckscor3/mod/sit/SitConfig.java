package bl4ckscor3.mod.sit;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.BoundedDiscrete;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "sit")
public class SitConfig implements ConfigData {
	@Comment("Disables the check to see whether both server and client have the same version of Sit installed.")
	boolean disableVersionChecker = false;
	//@formatter:off
	@Comment("""
			The maximum distance in blocks that the player can be away from a block to still be able to sit on it.
			Do note, that this is limited by how far a player can access a block in vanilla Minecraft.
			If this is set to 0, the player has to stand on top of the block to be able to sit on it.""")
	//@formatter:on
	@BoundedDiscrete(min = 0, max = 1000)
	int blockReachDistance = 4;
}