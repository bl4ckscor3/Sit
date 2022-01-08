package bl4ckscor3.mod.sit;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name="sit")
public class SitConfig implements ConfigData
{
	@Comment("Disables the check to see whether both server and client have the same version of Sit installed.")
	boolean disableVersionChecker = false;
}
