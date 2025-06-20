package bl4ckscor3.mod.sit;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Sit.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
public class SitClient {
	public SitClient(ModContainer modContainer) {
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}

	@SubscribeEvent
	public static void onFMLCLientSetup(FMLClientSetupEvent event) {
		EntityRenderers.register(Sit.SIT_ENTITY_TYPE.get(), NoopRenderer::new);
	}
}
