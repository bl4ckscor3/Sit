package bl4ckscor3.mod.sit;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

public class Sit implements ModInitializer
{
	public static final int PROTOCOL_VERSION = 11;
	public static final Identifier VERSION_CHECK = new Identifier("sit", "version_check");
	public static final Text INCORRECT_VERSION = new LiteralText(String.format("Please install Sit 1.16.5-%d to play on this server.", PROTOCOL_VERSION));
	public static final EntityType<SitEntity> SIT_ENTITY_TYPE = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier("sit", "entity_sit"),
			FabricEntityTypeBuilder.<SitEntity>create(SpawnGroup.MISC, SitEntity::new).dimensions(EntityDimensions.fixed(0.001F, 0.001F)).build()
	);

	@Override
	public void onInitialize()
	{
		//sit handling
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			BlockState s = world.getBlockState(hitResult.getBlockPos());
			Block b = world.getBlockState(hitResult.getBlockPos()).getBlock();

			if((b instanceof SlabBlock || b instanceof StairsBlock) && !SitEntity.OCCUPIED.containsKey(new Vec3d(hitResult.getBlockPos().getX(), hitResult.getBlockPos().getY(), hitResult.getBlockPos().getZ())) && player.getStackInHand(hand).isEmpty())
			{
				Vec3d comparePos = new Vec3d(player.getBlockPos().getX() + 0.5D, player.getBlockPos().getY() + 1.25D, player.getBlockPos().getZ() + 0.5D);

				//only allow sitting when rightclicking the top face of a block, and disallow sitting players from sitting again
				if(hitResult.getSide() != Direction.UP || SitEntity.OCCUPIED.containsKey(comparePos))
					return ActionResult.FAIL;

				if(b instanceof SlabBlock && (!s.getProperties().contains(SlabBlock.TYPE) || s.get(SlabBlock.TYPE) != SlabType.BOTTOM))
					return ActionResult.PASS;
				else if(b instanceof StairsBlock && (!s.getProperties().contains(StairsBlock.HALF) || s.get(StairsBlock.HALF) != BlockHalf.BOTTOM))
					return ActionResult.PASS;

				SitEntity sit = SIT_ENTITY_TYPE.create(world);
				Vec3d pos = new Vec3d(hitResult.getBlockPos().getX() + 0.5D, hitResult.getBlockPos().getY() + 0.25D, hitResult.getBlockPos().getZ() + 0.5D);

				SitEntity.OCCUPIED.put(pos, player.getBlockPos());
				sit.updatePosition(pos.getX(), pos.getY(), pos.getZ());
				world.spawnEntity(sit);
				player.startRiding(sit);
				return ActionResult.SUCCESS;
			}

			return ActionResult.PASS;
		});
	}
}
