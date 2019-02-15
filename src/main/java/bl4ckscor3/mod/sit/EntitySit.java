package bl4ckscor3.mod.sit;

import java.util.HashMap;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntitySit extends Entity
{
	public static final HashMap<BlockPos,EntitySit> OCCUPIED = new HashMap<BlockPos,EntitySit>();

	public EntitySit(World world)
	{
		super(Sit.SIT_ENTITY_TYPE, world);
		noClip = true;
		height = 0.0001F;
		width = 0.0001F;
	}

	public EntitySit(World world, BlockPos pos)
	{
		super(Sit.SIT_ENTITY_TYPE, world);
		setPosition(pos.getX() + 0.5D, pos.getY() + 0.25D, pos.getZ() + 0.5D);
		noClip = true;
		height = 0.0001F;
		width = 0.0001F;
		OCCUPIED.put(pos, this);
	}

	@Override
	protected void registerData()
	{}

	@Override
	protected void readAdditional(NBTTagCompound compound)
	{}

	@Override
	protected void writeAdditional(NBTTagCompound compound)
	{}
}