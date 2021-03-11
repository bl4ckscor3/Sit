package bl4ckscor3.mod.sit;

import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.BedPart;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityMountEvent;
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
		if(!event.getWorld().isRemote && event.getFace() == Direction.UP && !SitUtil.isPlayerSitting(event.getPlayer()))
		{
			World world = event.getWorld();
			BlockPos pos = event.getPos();
			BlockState state = world.getBlockState(pos);
			Block block = world.getBlockState(pos).getBlock();
			PlayerEntity player = event.getPlayer();

			if(isValidBlock(world, pos, state, block) && isPlayerInRange(player, pos) && !SitUtil.isOccupied(world, pos) && player.getHeldItemMainhand().isEmpty() && world.getBlockState(pos.up()).isAir(world, pos.up()))
			{
				if(block instanceof SlabBlock && (!state.has(SlabBlock.TYPE) || state.get(SlabBlock.TYPE) != SlabType.BOTTOM))
					return;
				else if(block instanceof StairsBlock && (!state.has(StairsBlock.HALF) || state.get(StairsBlock.HALF) != Half.BOTTOM))
					return;

				SitEntity sit = new SitEntity(world, pos);

				if(SitUtil.addSitEntity(world, pos, sit))
				{
					world.addEntity(sit);
					player.startRiding(sit);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onBreak(BreakEvent event)
	{
		if(!event.getWorld().isRemote())
		{
			SitEntity entity = SitUtil.getSitEntity(event.getWorld(), event.getPos());

			if(entity != null && SitUtil.removeSitEntity(event.getWorld(), event.getPos()))
				entity.remove();
		}
	}

	@SubscribeEvent
	public static void onEntityMount(EntityMountEvent event)
	{
		if(!event.getWorldObj().isRemote && event.isDismounting())
		{
			Entity player = event.getEntityBeingMounted();

			if(player instanceof SitEntity && SitUtil.removeSitEntity(event.getWorldObj(), player.getPosition()))
				player.remove();
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
	private static boolean isValidBlock(World world, BlockPos pos, BlockState state, Block block)
	{
		boolean isValid = block instanceof SlabBlock || block instanceof StairsBlock;

		if(!isValid && block instanceof BedBlock)
		{
			state = world.getBlockState(pos.offset(state.get(BedBlock.PART) == BedPart.HEAD ? state.get(BedBlock.HORIZONTAL_FACING).getOpposite() : state.get(BedBlock.HORIZONTAL_FACING)));

			if(!(state.getBlock() instanceof BedBlock)) //it's half a bed!
				isValid = true;
		}

		return isValid;
	}

	/**
	 * Returns whether or not the player is close enough to the block to be able to sit on it
	 * @param player The player
	 * @param pos The position of the block to sit on
	 * @return true if the player is close enough, false otherwhise
	 */
	private static boolean isPlayerInRange(PlayerEntity player, BlockPos pos)
	{
		BlockPos playerPos = player.getPosition();
		int blockReachDistance = Configuration.CONFIG.blockReachDistance.get();

		if(blockReachDistance == 0) //player has to stand on top of the block
			return playerPos.getY() - pos.getY() <= 1 && playerPos.getX() - pos.getX() == 0 && playerPos.getZ() - pos.getZ() == 0;

		pos = pos.add(0.5D, 0.5D, 0.5D);

		AxisAlignedBB range = new AxisAlignedBB(pos.getX() + blockReachDistance, pos.getY() + blockReachDistance, pos.getZ() + blockReachDistance, pos.getX() - blockReachDistance, pos.getY() - blockReachDistance, pos.getZ() - blockReachDistance);

		playerPos = playerPos.add(0.5D, 0.5D, 0.5D);
		return range.minX <= playerPos.getX() && range.minY <= playerPos.getY() && range.minZ <= playerPos.getZ() && range.maxX >= playerPos.getX() && range.maxY >= playerPos.getY() && range.maxZ >= playerPos.getZ();
	}
}
