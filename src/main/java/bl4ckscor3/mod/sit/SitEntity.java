package bl4ckscor3.mod.sit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;

public class SitEntity extends Entity
{
	public static final HashMap<Vec3d,BlockPos> OCCUPIED = new HashMap<>();

	public SitEntity(EntityType<? extends SitEntity> type, World world)
	{
		super(type, world);
	}

	public SitEntity(World world)
	{
		super(Sit.SIT_ENTITY_TYPE, world);
		noClip = true;
	}

	@Override
	public Vec3d updatePassengerForDismount(LivingEntity passenger)
	{
		if(passenger instanceof PlayerEntity)
		{
			BlockPos pos = OCCUPIED.remove(getPos());

			if(pos != null)
			{
				remove();
				return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
			}
		}

		remove();
		return super.updatePassengerForDismount(passenger);
	}

	@Override
	public void remove()
	{
		super.remove();

		OCCUPIED.remove(getPos());
	}


	@Override
	protected void initDataTracker() {}

	@Override
	public void readCustomDataFromTag(CompoundTag tag) {}

	@Override
	public void writeCustomDataToTag(CompoundTag tag) {}

	@Override
	public Packet<?> createSpawnPacket()
	{
		return new EntitySpawnS2CPacket(this);
	}
}
