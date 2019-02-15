package bl4ckscor3.mod.sit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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
		if(!event.getWorld().isRemote)
		{
			World w = event.getWorld();
			BlockPos p = event.getPos();
			IBlockState s = w.getBlockState(p);
			Block b = w.getBlockState(p).getBlock();
			EntityPlayer e = event.getEntityPlayer();

			if((isModBlock(b) || b instanceof BlockSlab || b instanceof BlockStairs) && !EntitySit.OCCUPIED.containsKey(p) && e.getHeldItemMainhand().isEmpty())
			{
				if(b instanceof BlockSlab && (!s.getProperties().containsKey(BlockSlab.HALF) || s.getValue(BlockSlab.HALF) != BlockSlab.EnumBlockHalf.BOTTOM))
					return;
				else if(b instanceof BlockStairs && (!s.getProperties().containsKey(BlockStairs.HALF) || s.getValue(BlockStairs.HALF) != BlockStairs.EnumHalf.BOTTOM))
					return;

				EntitySit sit = new EntitySit(w, p);

				w.spawnEntity(sit);
				e.startRiding(sit);
			}
		}
	}

	@SubscribeEvent
	public void onBreak(BreakEvent event)
	{
		if(EntitySit.OCCUPIED.containsKey(event.getPos()))
		{
			EntitySit.OCCUPIED.get(event.getPos()).setDead();
			EntitySit.OCCUPIED.remove(event.getPos());
		}
	}

	@SubscribeEvent
	public void onEntityMount(EntityMountEvent event)
	{
		if(event.isDismounting())
		{
			Entity e = event.getEntityBeingMounted();

			if(e instanceof EntitySit)
			{
				e.setDead();
				EntitySit.OCCUPIED.remove(e.getPosition());
			}
		}
	}

	/**
	 * Checks wether the given block is a specific block from a mod. Used to support
	 * stairs/slabs from other mods that don't work with Sit by default.
	 * @param b The block to check
	 * @return true if the block is a block to additionally support, false otherwise
	 */
	private boolean isModBlock(Block b)
	{
		if(Loader.isModLoaded("immersiveengineering") && b instanceof blusunrize.immersiveengineering.common.blocks.BlockIESlab)
			return true;
		else return false;
	}
}
