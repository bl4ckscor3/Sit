package bl4ckscor3.mod.sit;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.network.NetworkHooks;

public class SitEntity extends Entity
{
	public SitEntity(EntityType<SitEntity> type, Level world)
	{
		super(type, world);
	}

	public SitEntity(Level world, BlockPos pos)
	{
		super(Sit.SIT_ENTITY_TYPE, world);
		setPos(pos.getX() + 0.5D, pos.getY() + 0.25D, pos.getZ() + 0.5D);
		noPhysics = true;
	}

	@Override
	public Vec3 getDismountLocationForPassenger(LivingEntity passenger)
	{
		if(passenger instanceof Player)
		{
			BlockPos pos = SitUtil.getPreviousPlayerPosition((Player)passenger, this);

			if(pos != null)
			{
				remove();
				return new Vec3(pos.getX(), pos.getY(), pos.getZ());
			}
		}

		remove();
		return super.getDismountLocationForPassenger(passenger);
	}

	@Override
	public void remove()
	{
		super.remove();

		SitUtil.removeSitEntity(level, blockPosition());
	}

	@Override
	protected void defineSynchedData() {}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {}

	@Override
	public Packet<?> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}