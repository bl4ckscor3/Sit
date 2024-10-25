package bl4ckscor3.mod.sit;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(bus = Bus.MOD, value = Dist.CLIENT)
public class SitClient {
	private SitClient() {}

	@SubscribeEvent
	public static void onFMLCLientSetup(FMLClientSetupEvent event) {
		EntityRenderers.register(Sit.SIT_ENTITY_TYPE.get(), NoopRenderer::new);
	}
}
