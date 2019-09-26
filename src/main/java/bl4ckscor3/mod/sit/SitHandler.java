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
			World w = event.getWorld();
			BlockPos p = event.getPos();
			IBlockState s = w.getBlockState(p);
			Block b = w.getBlockState(p).getBlock();
			EntityPlayer e = event.getEntityPlayer();

			if((b instanceof BlockSlab || b instanceof BlockStairs || isModBlock(w, p, b)) && !SitUtil.isOccupied(w, p) && e.getHeldItemMainhand().isEmpty())
			{
				if(b instanceof BlockSlab && (!s.getProperties().containsKey(BlockSlab.HALF) || s.getValue(BlockSlab.HALF) != BlockSlab.EnumBlockHalf.BOTTOM))
					return;
				else if(b instanceof BlockStairs && (!s.getProperties().containsKey(BlockStairs.HALF) || s.getValue(BlockStairs.HALF) != BlockStairs.EnumHalf.BOTTOM))
					return;

				EntitySit sit = new EntitySit(w, p);

				if(SitUtil.addSitEntity(w, p, sit))
				{
					w.spawnEntity(sit);
					e.startRiding(sit);
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
			Entity e = event.getEntityBeingMounted();

			if(e instanceof EntitySit && SitUtil.removeSitEntity(event.getWorldObj(), e.getPosition()))
				e.setDead();
		}
	}

	/**
	 * Checks wether the given block is a specific block from a mod. Used to support
	 * stairs/slabs from other mods that don't work with Sit by default.
	 * @param w The world to check in
	 * @param p The position to check at
	 * @param b The block to check
	 * @return true if the block is a block to additionally support, false otherwise
	 */
	private boolean isModBlock(World w, BlockPos p, Block b)
	{
		if(Loader.isModLoaded("immersiveengineering") && b instanceof blusunrize.immersiveengineering.common.blocks.BlockIESlab)
			return true;
		else if(Loader.isModLoaded("architecturecraft") && b instanceof com.elytradev.architecture.common.block.BlockShape)
		{
			TileEntity te = w.getTileEntity(p);

			if(te instanceof com.elytradev.architecture.common.tile.TileShape)
			{
				return Arrays.asList(com.elytradev.architecture.common.shape.Shape.SLAB,
						com.elytradev.architecture.common.shape.Shape.STAIRS,
						com.elytradev.architecture.common.shape.Shape.STAIRS_INNER_CORNER,
						com.elytradev.architecture.common.shape.Shape.STAIRS_OUTER_CORNER
						).contains(((com.elytradev.architecture.common.tile.TileShape)te).shape);
			}
		}

		return false;
	}
}
