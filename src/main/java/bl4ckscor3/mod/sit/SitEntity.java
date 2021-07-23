package bl4ckscor3.mod.sit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class SitEntity extends Entity
{
	public SitEntity(EntityType<SitEntity> type, World world)
	{
		super(type, world);
	}

	public SitEntity(World world, BlockPos pos)
	{
		super(Sit.SIT_ENTITY_TYPE, world);
		setPos(pos.getX() + 0.5D, pos.getY() + 0.25D, pos.getZ() + 0.5D);
		noPhysics = true;
	}

	@Override
	public Vector3d getDismountLocationForPassenger(LivingEntity passenger)
	{
		if(passenger instanceof PlayerEntity)
		{
			BlockPos pos = SitUtil.getPreviousPlayerPosition((PlayerEntity)passenger, this);

			if(pos != null)
			{
				remove();
				return new Vector3d(pos.getX(), pos.getY(), pos.getZ());
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
	protected void readAdditionalSaveData(CompoundNBT tag) {}

	@Override
	protected void addAdditionalSaveData(CompoundNBT tag) {}

	@Override
	public IPacket<?> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}