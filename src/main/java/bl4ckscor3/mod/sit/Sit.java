package bl4ckscor3.mod.sit;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.SharedConstants;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class Sit implements ModInitializer
{
	public static final ResourceLocation VERSION_CHECK = new ResourceLocation("sit", "version_check");
	public static final Component INCORRECT_VERSION = new TextComponent(String.format("Please install Sit %d for Minecraft %s to play on this server.", getModVersion(), getMajorMinecraftVersion()));
	public static final EntityType<SitEntity> SIT_ENTITY_TYPE = Registry.register(
			Registry.ENTITY_TYPE,
			new ResourceLocation("sit", "entity_sit"),
			FabricEntityTypeBuilder.<SitEntity>create(MobCategory.MISC, SitEntity::new).dimensions(EntityDimensions.fixed(0.001F, 0.001F)).build()
	);

	@Override
	public void onInitialize()
	{
		//sit handling
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if(world.isClientSide)
				return InteractionResult.PASS;

			if(!world.mayInteract(player, hitResult.getBlockPos()))
				return InteractionResult.PASS;

			BlockState s = world.getBlockState(hitResult.getBlockPos());
			Block b = world.getBlockState(hitResult.getBlockPos()).getBlock();

			if((b instanceof SlabBlock || b instanceof StairBlock) && !SitEntity.OCCUPIED.containsKey(new Vec3(hitResult.getBlockPos().getX(), hitResult.getBlockPos().getY(), hitResult.getBlockPos().getZ())) && player.getItemInHand(hand).isEmpty())
			{
				Vec3 comparePos = new Vec3(player.blockPosition().getX() + 0.5D, player.blockPosition().getY() + 1.25D, player.blockPosition().getZ() + 0.5D);

				//only allow sitting when rightclicking the top face of a block, and disallow sitting players from sitting again
				if(hitResult.getDirection() != Direction.UP || SitEntity.OCCUPIED.containsKey(comparePos))
					return InteractionResult.PASS;

				if(b instanceof SlabBlock && (!s.getProperties().contains(SlabBlock.TYPE) || s.getValue(SlabBlock.TYPE) != SlabType.BOTTOM))
					return InteractionResult.PASS;
				else if(b instanceof StairBlock && (!s.getProperties().contains(StairBlock.HALF) || s.getValue(StairBlock.HALF) != Half.BOTTOM))
					return InteractionResult.PASS;

				SitEntity sit = SIT_ENTITY_TYPE.create(world);
				Vec3 pos = new Vec3(hitResult.getBlockPos().getX() + 0.5D, hitResult.getBlockPos().getY() + 0.25D, hitResult.getBlockPos().getZ() + 0.5D);

				SitEntity.OCCUPIED.put(pos, player.blockPosition());
				sit.absMoveTo(pos.x(), pos.y(), pos.z());
				world.addFreshEntity(sit);
				player.startRiding(sit);
				return InteractionResult.SUCCESS;
			}

			return InteractionResult.PASS;
		});
	}

	private static String getMajorMinecraftVersion()
	{
		String version = SharedConstants.VERSION_STRING;
		String[] versionSplit = version.split("\\.");

		if(versionSplit.length > 2)
			return versionSplit[0] + "." + versionSplit[1];
		else
			return version;
	}

	public static int getModVersion()
	{
		Optional<ModContainer> modContainer = FabricLoaderImpl.INSTANCE.getModContainer("sit");

		if(modContainer.isPresent())
		{
			try
			{
				return Integer.parseInt(modContainer.get().getMetadata().getVersion().getFriendlyString().split("-")[1]); //Sit's format is mcversion-modversion
			}
			catch(NumberFormatException e) {}
		}

		return 0;
	}
}
