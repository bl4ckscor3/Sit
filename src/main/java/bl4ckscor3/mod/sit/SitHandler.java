package bl4ckscor3.mod.sit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class SitHandler
{
	@SubscribeEvent
	public static void onRightClickBlock(RightClickBlock event)
	{
		if(!event.getWorld().isRemote)
		{
			World w = event.getWorld();
			BlockPos p = event.getPos();
			BlockState s = w.getBlockState(p);
			Block b = w.getBlockState(p).getBlock();
			PlayerEntity e = event.getEntityPlayer();

			if((b instanceof SlabBlock || b instanceof StairsBlock || isModBlock(b)) && !EntitySit.OCCUPIED.containsKey(p) && e.getHeldItemMainhand().isEmpty())
			{
				if(b instanceof SlabBlock && (!s.has(SlabBlock.TYPE) || s.get(SlabBlock.TYPE) != SlabType.BOTTOM))
					return;
				else if(b instanceof StairsBlock && (!s.has(StairsBlock.HALF) || s.get(StairsBlock.HALF) != Half.BOTTOM))
					return;

				EntitySit sit = new EntitySit(w, p);

				w.addEntity(sit);
				e.startRiding(sit);
			}
		}
	}

	@SubscribeEvent
	public static void onBreak(BreakEvent event)
	{
		if(EntitySit.OCCUPIED.containsKey(event.getPos()))
		{
			EntitySit.OCCUPIED.get(event.getPos()).remove();
			EntitySit.OCCUPIED.remove(event.getPos());
		}
	}

	@SubscribeEvent
	public static void onEntityMount(EntityMountEvent event)
	{
		if(event.isDismounting())
		{
			Entity e = event.getEntityBeingMounted();

			if(e instanceof EntitySit)
			{
				e.remove();
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
	private static boolean isModBlock(Block b)
	{
		/*		if(ModList.get().isLoaded("immersiveengineering") && b instanceof blusunrize.immersiveengineering.common.blocks.BlockIESlab)
					return true;
		else*/ if(ModList.get().isLoaded("snowvariants") && b instanceof trikzon.snowvariants.blocks.SnowSlab)
			return true;
		else return false;
	}
}
