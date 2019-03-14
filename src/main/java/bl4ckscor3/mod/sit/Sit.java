package bl4ckscor3.mod.sit;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.registry.Registry;

public class Sit implements ModInitializer
{
	public static final EntityType<Entity> ENTITY_SIT = Registry.register(Registry.ENTITY_TYPE, "sit:entity_sit", FabricEntityTypeBuilder.create(EntityCategory.MISC, (type, world) -> new EntitySit(world)).build());

	@Override
	public void onInitialize()
	{
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			BlockState s = world.getBlockState(hitResult.getBlockPos());
			Block b = world.getBlockState(hitResult.getBlockPos()).getBlock();

			if((b instanceof SlabBlock || b instanceof StairsBlock) && !EntitySit.OCCUPIED.containsKey(hitResult.getBlockPos()) && player.getStackInHand(hand).isEmpty())
			{
				if(b instanceof SlabBlock && (!s.getProperties().contains(SlabBlock.TYPE) || s.get(SlabBlock.TYPE) != SlabType.BOTTOM))
					return ActionResult.PASS;
				else if(b instanceof StairsBlock && (!s.getProperties().contains(StairsBlock.HALF) || s.get(StairsBlock.HALF) != BlockHalf.BOTTOM))
					return ActionResult.PASS;

				EntitySit sit = new EntitySit(world, hitResult.getBlockPos());

				world.spawnEntity(sit);
				player.startRiding(sit);
				return ActionResult.SUCCESS;
			}

			return ActionResult.PASS;
		});

		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			if(EntitySit.OCCUPIED.containsKey(pos))
				EntitySit.OCCUPIED.get(pos).kill();

			return ActionResult.PASS;
		});
	}
}
