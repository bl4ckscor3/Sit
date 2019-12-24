package bl4ckscor3.mod.sit;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
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
		if(!event.getWorld().isRemote && event.getFace() == Direction.UP && !SitUtil.isPlayerSitting(event.getPlayer()))
		{
			World world = event.getWorld();
			BlockPos pos = event.getPos();
			BlockState state = world.getBlockState(pos);
			Block block = world.getBlockState(pos).getBlock();
			PlayerEntity player = event.getPlayer();

			if((block instanceof SlabBlock || block instanceof StairsBlock || isModBlock(block)) && !SitUtil.isOccupied(world, pos) && player.getHeldItemMainhand().isEmpty() && world.getBlockState(pos.up()).isAir(world, pos.up()))
			{
				if(block instanceof SlabBlock && (!state.has(SlabBlock.TYPE) || state.get(SlabBlock.TYPE) != SlabType.BOTTOM))
					return;
				else if(block instanceof StairsBlock && (!state.has(StairsBlock.HALF) || state.get(StairsBlock.HALF) != Half.BOTTOM))
					return;

				EntitySit sit = new EntitySit(world, pos);

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
			EntitySit entity = SitUtil.getSitEntity(event.getWorld(), event.getPos());

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

			if(player instanceof EntitySit && SitUtil.removeSitEntity(event.getWorldObj(), player.getPosition()))
				player.remove();
		}
	}

	/**
	 * Checks wether the given block is a specific block from a mod. Used to support
	 * stairs/slabs from other mods that don't work with Sit by default.
	 * @param block The block to check
	 * @return true if the block is a block to additionally support, false otherwise
	 */
	private static boolean isModBlock(Block block)
	{
		/*		if(ModList.get().isLoaded("immersiveengineering") && b instanceof blusunrize.immersiveengineering.common.blocks.BlockIESlab)
					return true;
		else*/ if(ModList.get().isLoaded("snowvariants") && block instanceof trikzon.snowvariants.blocks.SnowSlab)
			return true;
		else return false;
	}
}
