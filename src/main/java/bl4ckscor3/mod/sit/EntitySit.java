package bl4ckscor3.mod.sit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntitySit extends Entity
{
	public EntitySit(EntityType<EntitySit> type, World world)
	{
		super(type, world);
	}

	public EntitySit(World world, BlockPos pos)
	{
		super(Sit.SIT_ENTITY_TYPE, world);
		setPosition(pos.getX() + 0.5D, pos.getY() + 0.25D, pos.getZ() + 0.5D);
		noClip = true;
	}

	@Override
	protected void registerData() {}

	@Override
	protected void readAdditional(CompoundNBT tag) {}

	@Override
	protected void writeAdditional(CompoundNBT tag) {}

	@Override
	public IPacket<?> createSpawnPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}