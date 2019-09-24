package bl4ckscor3.mod.sit;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntitySit extends Entity
{
	public EntitySit(World world)
	{
		super(world);
		noClip = true;
		height = 0.0001F;
		width = 0.0001F;
	}

	public EntitySit(World world, BlockPos pos)
	{
		super(world);
		setPosition(pos.getX() + 0.5D, pos.getY() + 0.25D, pos.getZ() + 0.5D);
		noClip = true;
		height = 0.0001F;
		width = 0.0001F;
	}

	@Override
	protected void entityInit()
	{}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag)
	{}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag)
	{}
}