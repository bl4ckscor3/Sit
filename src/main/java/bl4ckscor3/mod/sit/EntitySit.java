package bl4ckscor3.mod.sit;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntitySit extends Entity
{
	//<dimension type id, <position, entity>>
	public static final Map<Integer,Map<BlockPos,EntitySit>> OCCUPIED = new HashMap<>();

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

		if(!world.isRemote)
		{
			int id = world.provider.getDimension();

			if(!OCCUPIED.containsKey(id))
				OCCUPIED.put(id, new HashMap<>());

			OCCUPIED.get(id).put(pos, this);
		}
	}

	@Override
	protected void entityInit()
	{}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{}
}