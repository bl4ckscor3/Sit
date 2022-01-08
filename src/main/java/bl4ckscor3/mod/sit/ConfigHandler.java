package bl4ckscor3.mod.sit;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class ConfigHandler
{
	public static void register()
	{
		AutoConfig.register(SitConfig.class, JanksonConfigSerializer::new);
	}

	public static boolean versionCheckDisabled()
	{
		return AutoConfig.getConfigHolder(SitConfig.class).getConfig().disableVersionChecker;
	}
}
