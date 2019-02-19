package bl4ckscor3.mod.sit;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class SitHandler
{
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		try
		{
			if(!event.world.isRemote && event.action == Action.RIGHT_CLICK_BLOCK)
			{
				World w = event.world;
				EntityPlayer e = event.entityPlayer;
				Block b = w.getBlock(event.x, event.y, event.z);
				BlockPos p = new BlockPos(event.x, event.y, event.z);

				if(!EntitySit.OCCUPIED.containsKey(p) && e.getHeldItem() == null)
				{
					if(b instanceof BlockSlab)
					{
						if((w.getBlockMetadata(p.x, p.y, p.z) & 8) != 0)
							return;
					}
					else if(b instanceof BlockStairs)
					{
						if((w.getBlockMetadata(p.x, p.y, p.z) & 4) != 0)
							return;
					}
					else if(Loader.isModLoaded("ImmersiveEngineering") && b instanceof blusunrize.immersiveengineering.common.blocks.BlockIESlabs)
					{
						TileEntity te = w.getTileEntity(event.x, event.y, event.z);

						if((te instanceof blusunrize.immersiveengineering.common.blocks.TileEntityIESlab) && ((blusunrize.immersiveengineering.common.blocks.TileEntityIESlab)te).slabType != 0)
							return;
					}
					else return;

					EntitySit sit = new EntitySit(w, p);

					w.spawnEntityInWorld(sit);
					e.mountEntity(sit);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@SubscribeEvent
	public void onBreak(BreakEvent event)
	{
		BlockPos pos = new BlockPos(event.x, event.y, event.z);

		if(EntitySit.OCCUPIED.containsKey(pos))
		{
			EntitySit.OCCUPIED.get(pos).setDead();
			EntitySit.OCCUPIED.remove(pos);
		}
	}
}
