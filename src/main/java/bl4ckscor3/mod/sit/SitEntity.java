package bl4ckscor3.mod.sit;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;

public class SitEntity extends Entity
{
	public static final HashMap<Vec3,BlockPos> OCCUPIED = new HashMap<>();

	public SitEntity(EntityType<? extends SitEntity> type, Level world)
	{
		super(type, world);
	}

	public SitEntity(Level world)
	{
		super(Sit.SIT_ENTITY_TYPE, world);
		noPhysics = true;
	}

	@Override
	public Vec3 getDismountLocationForPassenger(LivingEntity passenger)
	{
		if(passenger instanceof Player)
		{
			BlockPos pos = OCCUPIED.remove(position());

			if(pos != null)
			{
				remove(RemovalReason.DISCARDED);
				return new Vec3(pos.getX(), pos.getY(), pos.getZ());
			}
		}

		remove(RemovalReason.DISCARDED);
		return super.getDismountLocationForPassenger(passenger);
	}

	@Override
	public void remove(RemovalReason reason)
	{
		super.remove(reason);

		OCCUPIED.remove(position());
	}

	@Override
	protected void defineSynchedData() {}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {}

	@Override
	public Packet<?> getAddEntityPacket()
	{
		return new ClientboundAddEntityPacket(this);
	}
}
