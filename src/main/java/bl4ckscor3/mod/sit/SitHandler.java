package bl4ckscor3.mod.sit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=Sit.MODID)
public class SitHandler
{
	@SubscribeEvent
	public static void onRightClickBlock(RightClickBlock event)
	{
		Player player = event.getPlayer();

		if(!event.getWorld().isClientSide && event.getFace() == Direction.UP && !SitUtil.isPlayerSitting(player) && !player.isShiftKeyDown())
		{
			Level world = event.getWorld();
			BlockPos pos = event.getPos();
			BlockState state = world.getBlockState(pos);
			Block block = world.getBlockState(pos).getBlock();

			if(isValidBlock(world, pos, state, block) && isPlayerInRange(player, pos) && !SitUtil.isOccupied(world, pos) && player.getMainHandItem().isEmpty() && world.getBlockState(pos.above()).isAir())
			{
				if(block instanceof SlabBlock && (!state.hasProperty(SlabBlock.TYPE) || state.getValue(SlabBlock.TYPE) != SlabType.BOTTOM))
					return;
				else if(block instanceof StairBlock && (!state.hasProperty(StairBlock.HALF) || state.getValue(StairBlock.HALF) != Half.BOTTOM))
					return;

				SitEntity sit = new SitEntity(world, pos);

				if(SitUtil.addSitEntity(world, pos, sit, player.blockPosition()))
				{
					world.addFreshEntity(sit);
					player.startRiding(sit);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onBreak(BreakEvent event)
	{
		if(!event.getWorld().isClientSide())
		{
			//BreakEvent gets a World in its constructor, so the cast is safe
			SitEntity entity = SitUtil.getSitEntity((Level)event.getWorld(), event.getPos());

			if(entity != null)
			{
				SitUtil.removeSitEntity((Level)event.getWorld(), event.getPos());
				entity.ejectPassengers();
			}
		}
	}

	/**
	 * Returns whether or not the given block can be sat on
	 * @param world The world to check in
	 * @param pos The position to check at
	 * @param state The block state at the given position in the given world
	 * @param block The block to check
	 * @return true if the given block can be sat one, false otherwhise
	 */
	private static boolean isValidBlock(Level world, BlockPos pos, BlockState state, Block block)
	{
		boolean isValid = block instanceof SlabBlock || block instanceof StairBlock || isModBlock(block);

		if(!isValid && block instanceof BedBlock)
		{
			state = world.getBlockState(pos.relative(state.getValue(BedBlock.PART) == BedPart.HEAD ? state.getValue(BedBlock.FACING).getOpposite() : state.getValue(BedBlock.FACING)));

			if(!(state.getBlock() instanceof BedBlock)) //it's half a bed!
				isValid = true;
		}

		return isValid;
	}

	/**
	 * Checks whether the given block is a specific block from a mod. Used to support
	 * stairs/slabs from other mods that don't work with Sit by default.
	 * @param block The block to check
	 * @return true if the block is a block to additionally support, false otherwise
	 */
	private static boolean isModBlock(Block block)
	{
		/*		if(ModList.get().isLoaded("immersiveengineering") && b instanceof blusunrize.immersiveengineering.common.blocks.BlockIESlab)
					return true;
		else if(ModList.get().isLoaded("snowvariants") && block instanceof trikzon.snowvariants.blocks.SnowSlab)
			return true;
		else*/ return false;
	}

	/**
	 * Returns whether or not the player is close enough to the block to be able to sit on it
	 * @param player The player
	 * @param pos The position of the block to sit on
	 * @return true if the player is close enough, false otherwhise
	 */
	private static boolean isPlayerInRange(Player player, BlockPos pos)
	{
		BlockPos playerPos = player.blockPosition();
		int blockReachDistance = Configuration.CONFIG.blockReachDistance.get();

		if(blockReachDistance == 0) //player has to stand on top of the block
			return playerPos.getY() - pos.getY() <= 1 && playerPos.getX() - pos.getX() == 0 && playerPos.getZ() - pos.getZ() == 0;

		pos = pos.offset(0.5D, 0.5D, 0.5D);

		AABB range = new AABB(pos.getX() + blockReachDistance, pos.getY() + blockReachDistance, pos.getZ() + blockReachDistance, pos.getX() - blockReachDistance, pos.getY() - blockReachDistance, pos.getZ() - blockReachDistance);

		playerPos = playerPos.offset(0.5D, 0.5D, 0.5D);
		return range.minX <= playerPos.getX() && range.minY <= playerPos.getY() && range.minZ <= playerPos.getZ() && range.maxX >= playerPos.getX() && range.maxY >= playerPos.getY() && range.maxZ >= playerPos.getZ();
	}
}
