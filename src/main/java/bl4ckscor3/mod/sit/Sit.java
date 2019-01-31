package bl4ckscor3.mod.sit;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.events.PlayerInteractionEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.registry.Registry;

public class Sit implements ModInitializer
{
	public static final EntityType<EntitySit> ENTITY_SIT = Registry.register(Registry.ENTITY_TYPE, "sit:entity_sit", FabricEntityTypeBuilder.create(EntitySit.class, EntitySit::new).build());

	@Override
	public void onInitialize()
	{
		PlayerInteractionEvent.INTERACT_BLOCK.register((player, world, hand, pos, direction, v, v1, v2) -> {
			BlockState s = world.getBlockState(pos);
			Block b = world.getBlockState(pos).getBlock();

			if((b instanceof SlabBlock || b instanceof StairsBlock) && !EntitySit.OCCUPIED.containsKey(pos) && player.getStackInHand(hand).isEmpty())
			{
				if(b instanceof SlabBlock && (!s.getProperties().contains(SlabBlock.TYPE) || s.get(SlabBlock.TYPE) != SlabType.BOTTOM))
					return ActionResult.PASS;
				else if(b instanceof StairsBlock && (!s.getProperties().contains(StairsBlock.HALF) || s.get(StairsBlock.HALF) != BlockHalf.BOTTOM))
					return ActionResult.PASS;

				EntitySit sit = new EntitySit(world, pos);

				world.spawnEntity(sit);
				player.startRiding(sit);
				return ActionResult.SUCCESS;
			}

			return ActionResult.PASS;
		});

		PlayerInteractionEvent.ATTACK_BLOCK.register((player, world, hand, pos, direction) -> {
			if(EntitySit.OCCUPIED.containsKey(pos))
				EntitySit.OCCUPIED.get(pos).kill();

			return ActionResult.PASS;
		});
	}
}
