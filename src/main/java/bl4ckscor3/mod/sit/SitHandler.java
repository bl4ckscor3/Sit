package bl4ckscor3.mod.sit;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SitHandler
{
	@SubscribeEvent
	public void onRightClickBlock(RightClickBlock event)
	{
		if(!event.getWorld().isRemote && event.getFace() == EnumFacing.UP && !SitUtil.isPlayerSitting(event.getEntityPlayer()))
		{
			World world = event.getWorld();
			BlockPos pos = event.getPos();
			IBlockState state = world.getBlockState(pos);
			Block block = world.getBlockState(pos).getBlock();
			EntityPlayer player = event.getEntityPlayer();

			if((block instanceof BlockSlab || block instanceof BlockStairs || isModBlock(world, pos, block)) && !SitUtil.isOccupied(world, pos) && player.getHeldItemMainhand().isEmpty())
			{
				IBlockState stateAbove = world.getBlockState(pos.up());

				if(!stateAbove.getBlock().isAir(stateAbove, world, pos.up()))
					return;
				else if(block instanceof BlockSlab && (!state.getProperties().containsKey(BlockSlab.HALF) || state.getValue(BlockSlab.HALF) != BlockSlab.EnumBlockHalf.BOTTOM))
					return;
				else if(block instanceof BlockStairs && (!state.getProperties().containsKey(BlockStairs.HALF) || state.getValue(BlockStairs.HALF) != BlockStairs.EnumHalf.BOTTOM))
					return;

				EntitySit sit = new EntitySit(world, pos);

				if(SitUtil.addSitEntity(world, pos, sit))
				{
					world.spawnEntity(sit);
					player.startRiding(sit);
				}
			}
		}
	}

	@SubscribeEvent
	public void onBreak(BreakEvent event)
	{
		if(!event.getWorld().isRemote)
		{
			EntitySit entity = SitUtil.getSitEntity(event.getWorld(), event.getPos());

			if(entity != null && SitUtil.removeSitEntity(event.getWorld(), event.getPos()))
				entity.setDead();
		}
	}

	@SubscribeEvent
	public void onEntityMount(EntityMountEvent event)
	{
		if(!event.getWorldObj().isRemote && event.isDismounting())
		{
			Entity entity = event.getEntityBeingMounted();

			if(entity instanceof EntitySit && SitUtil.removeSitEntity(event.getWorldObj(), entity.getPosition()))
				entity.setDead();
		}
	}

	/**
	 * Checks whether the given block is a specific block from a mod. Used to support stairs/slabs from other mods that don't work with Sit by default.
	 * @param world The world to check in
	 * @param pos The position to check at
	 * @param block The block to check
	 * @return true if the block is a block to additionally support, false otherwise
	 */
	private boolean isModBlock(World world, BlockPos pos, Block block)
	{
		if(Loader.isModLoaded("immersiveengineering") && block instanceof blusunrize.immersiveengineering.common.blocks.BlockIESlab)
			return true;
		else if(Loader.isModLoaded("architecturecraft") && block instanceof com.elytradev.architecture.common.block.BlockShape)
		{
			TileEntity te = world.getTileEntity(pos);

			if(te instanceof com.elytradev.architecture.common.tile.TileShape)
			{
				return Arrays.asList(com.elytradev.architecture.common.shape.Shape.SLAB,
						com.elytradev.architecture.common.shape.Shape.STAIRS,
						com.elytradev.architecture.common.shape.Shape.STAIRS_INNER_CORNER,
						com.elytradev.architecture.common.shape.Shape.STAIRS_OUTER_CORNER
						).contains(((com.elytradev.architecture.common.tile.TileShape)te).getShape());
			}
		}

		return false;
	}
}
