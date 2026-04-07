package bl4ckscor3.mod.sit;

import java.util.Optional;
import java.util.function.Supplier;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.neoforged.fml.config.ModConfig;

public class FabricEntrypoint implements ModInitializer, Platform {
	@Override
	public void onInitialize() {
		UseBlockCallback.EVENT.register((player, level, _, hitResult) -> SitHandler.onRightClickBlock(player, level, hitResult.getDirection(), hitResult.getBlockPos()));
		PlayerBlockBreakEvents.AFTER.register((level, _, pos, _, _) -> SitHandler.onBreak(level, pos));
		Sit.initialize(this);
		ConfigRegistry.INSTANCE.register(Sit.MODID, ModConfig.Type.SERVER, Configuration.CONFIG_SPEC);
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public <R, T extends R> void register(ResourceKey<? extends Registry<R>> registryKey, Supplier<T> entry, String path) {
		Optional<Holder.Reference<R>> registry = BuiltInRegistries.REGISTRY.get((ResourceKey) registryKey);

		if (registry.isEmpty()) {
			throw new IllegalArgumentException("Couldn't find registry " + registryKey);
		}

		Registry.register((Registry<R>) registry.get().value(), Sit.id(path), entry.get());
	}
}
