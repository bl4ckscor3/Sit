package bl4ckscor3.mod.sit;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.AABB;

public class Sit implements ModInitializer {
	public static final ResourceLocation VERSION_CHECK = new ResourceLocation("sit", "version_check");
	public static final Component INCORRECT_VERSION = Component.literal(String.format("Please install Sit %d for Minecraft %s to play on this server.", getModVersion(), getMajorMinecraftVersion()));
	//@formatter:off
	public static final EntityType<SitEntity> SIT_ENTITY_TYPE = Registry.register(
			BuiltInRegistries.ENTITY_TYPE,
			new ResourceLocation("sit", "entity_sit"),
			FabricEntityTypeBuilder.<SitEntity>create(MobCategory.MISC, SitEntity::new).dimensions(EntityDimensions.fixed(0.001F, 0.001F)).build()
	);
	//@formatter:on

	@Override
	public void onInitialize() {
		AutoConfig.register(SitConfig.class, JanksonConfigSerializer::new);
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

			if ((b instanceof SlabBlock || b instanceof StairBlock) && isPlayerInRange(player, hitPos) && !SitUtil.isOccupied(level, hitPos) && player.getItemInHand(hand).isEmpty()) {
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

	/**
	 * Returns whether the player is close enough to the block to be able to sit on it
	 *
	 * @param player The player
	 * @param pos The position of the block to sit on
	 * @return true if the player is close enough, false otherwise
	 */
	private static boolean isPlayerInRange(Player player, BlockPos pos) {
		BlockPos playerPos = player.blockPosition();
		int blockReachDistance = AutoConfig.getConfigHolder(SitConfig.class).getConfig().blockReachDistance;

		if (blockReachDistance == 0) //player has to stand on top of the block
			return playerPos.getY() - pos.getY() <= 1 && playerPos.getX() - pos.getX() == 0 && playerPos.getZ() - pos.getZ() == 0;

		AABB range = new AABB(pos.getX() + blockReachDistance, pos.getY() + blockReachDistance, pos.getZ() + blockReachDistance, pos.getX() - blockReachDistance, pos.getY() - blockReachDistance, pos.getZ() - blockReachDistance);

		return range.minX <= playerPos.getX() && range.minY <= playerPos.getY() && range.minZ <= playerPos.getZ() && range.maxX >= playerPos.getX() && range.maxY >= playerPos.getY() && range.maxZ >= playerPos.getZ();
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
			catch (Exception e) {
				LogManager.getLogger().error("Couldn't find proper Sit version. Version is: " + modContainer.get().getMetadata().getVersion().getFriendlyString());
			}
		}

		return 0;
	}
}