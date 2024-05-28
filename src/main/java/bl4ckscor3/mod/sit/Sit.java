package bl4ckscor3.mod.sit;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
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
	//@formatter:off
	public static final EntityType<SitEntity> SIT_ENTITY_TYPE = Registry.register(
			BuiltInRegistries.ENTITY_TYPE,
			new ResourceLocation("sit", "entity_sit"),
			EntityType.Builder.<SitEntity>of(SitEntity::new, MobCategory.MISC).sized(0.001F, 0.001F).build()
	);
	//@formatter:on

	@Override
	public void onInitialize() {
		AutoConfig.register(SitConfig.class, JanksonConfigSerializer::new);
		//sit handling
		UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
			if (level.isClientSide || !level.mayInteract(player, hitResult.getBlockPos()) || player.isShiftKeyDown() || SitUtil.isPlayerSitting(player) || hitResult.getDirection() != Direction.UP)
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

				sit.absMoveTo(hitPos.getX() + 0.5D, hitPos.getY() + 0.5D, hitPos.getZ() + 0.5D);

				if (SitUtil.addSitEntity(level, hitPos, sit, player.position())) {
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
}