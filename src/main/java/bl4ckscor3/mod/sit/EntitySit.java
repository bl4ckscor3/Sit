package bl4ckscor3.mod.sit;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntitySit extends Entity
{
	public static final HashMap<BlockPos,EntitySit> OCCUPIED = new HashMap<BlockPos,EntitySit>();

	public EntitySit(World world)
	{
		super(Sit.ENTITY_SIT, world);
		noClip = true;
	}

	public EntitySit(World world, BlockPos pos)
	{
		super(Sit.ENTITY_SIT, world);
		setPosition(pos.getX() + 0.5D, pos.getY() + 0.25D, pos.getZ() + 0.5D);
		noClip = true;
		OCCUPIED.put(pos, this);
	}

	@Override
	public void stopRiding()
	{
		if(getRiddenEntity() != null)
			kill();

		super.stopRiding();
	}

	@Override
	public void kill()
	{
		EntitySit.OCCUPIED.remove(getPos());
		super.kill();
	}

	@Override
	public  void initDataTracker() {}

	@Override
	public void readCustomDataFromTag(CompoundTag tag) {}

	@Override
	public void writeCustomDataToTag(CompoundTag tag) {}

	@Override
	public Packet<?> createSpawnPacket()
	{
		return null;
	}
}