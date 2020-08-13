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
		setPosition(pos.getX() + 0.5D, pos.getY() + 0.25D, pos.getZ() + 0.5D);
		noClip = true;
	}

	@Override
	public Vector3d func_230268_c_(LivingEntity passenger) //getPositionAfterDismount
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
		return super.func_230268_c_(passenger);
	}

	@Override
	public void remove()
	{
		super.remove();

		SitUtil.removeSitEntity(world, getPosition());
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