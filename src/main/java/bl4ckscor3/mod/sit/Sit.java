package bl4ckscor3.mod.sit;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;

import java.util.Optional;

public class Sit implements ModInitializer {
	public static final ResourceLocation VERSION_CHECK = new ResourceLocation("sit", "version_check");
	public static final Component INCORRECT_VERSION = Component.literal(String.format("Please install Sit %d for Minecraft %s to play on this server.", getModVersion(), getMajorMinecraftVersion()));
	public static final EntityType<SitEntity> SIT_ENTITY_TYPE = Registry.register(
			BuiltInRegistries.ENTITY_TYPE,
			new ResourceLocation("sit", "entity_sit"),
			FabricEntityTypeBuilder.<SitEntity>create(MobCategory.MISC, SitEntity::new).dimensions(EntityDimensions.fixed(0.001F, 0.001F)).build()
	);

	@Override
	public void onInitialize() {
		//sit handling
		UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
			if (level.isClientSide)
				return InteractionResult.PASS;

			if (!level.mayInteract(player, hitResult.getBlockPos()))
				return InteractionResult.PASS;

			if (player.isShiftKeyDown())
				return InteractionResult.PASS;

			if (SitUtil.isPlayerSitting(player))
				return InteractionResult.PASS;

			if (hitResult.getDirection() != Direction.UP)
				return InteractionResult.PASS;

			BlockPos hitPos = hitResult.getBlockPos();
			BlockState s = level.getBlockState(hitPos);
			Block b = s.getBlock();

			if ((b instanceof SlabBlock || b instanceof StairBlock) && !SitUtil.isOccupied(level, hitPos) && player.getItemInHand(hand).isEmpty()) {
				if (b instanceof SlabBlock && (!s.getProperties().contains(SlabBlock.TYPE) || s.getValue(SlabBlock.TYPE) != SlabType.BOTTOM))
					return InteractionResult.PASS;
				else if (b instanceof StairBlock && (!s.getProperties().contains(StairBlock.HALF) || s.getValue(StairBlock.HALF) != Half.BOTTOM))
					return InteractionResult.PASS;

				SitEntity sit = SIT_ENTITY_TYPE.create(level);

				sit.absMoveTo(hitPos.getX() + 0.5D, hitPos.getY() + 0.25D, hitPos.getZ() + 0.5D);

				if (SitUtil.addSitEntity(level, hitPos, sit, player.blockPosition())) {
					level.addFreshEntity(sit);
					player.startRiding(sit);
					return InteractionResult.SUCCESS;
				}
			}

			return InteractionResult.PASS;
		});
		PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
			if (!level.isClientSide) {
				SitEntity entity = SitUtil.getSitEntity(level, pos);

				if (entity != null) {
					SitUtil.removeSitEntity(level, pos);
					entity.ejectPassengers();
				}
			}
		});
	}

	private static String getMajorMinecraftVersion() {
		String version = SharedConstants.VERSION_STRING;
		String[] versionSplit = version.split("\\.");

		if (versionSplit.length > 2)
			return versionSplit[0] + "." + versionSplit[1];
		else
			return version;
	}

	public static int getModVersion() {
		Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer("sit");

		if (modContainer.isPresent()) {
			try {
				return Integer.parseInt(modContainer.get().getMetadata().getVersion().getFriendlyString().split("-")[1]); //Sit's format is mcversion-modversion
			}
			catch (NumberFormatException e) {}
		}

		return 0;
	}
}