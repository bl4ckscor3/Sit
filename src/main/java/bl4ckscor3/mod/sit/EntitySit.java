package bl4ckscor3.mod.sit;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntitySit extends Entity
{
	public static final HashMap<BlockPos, EntitySit> OCCUPIED = new HashMap<BlockPos, EntitySit>();

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
		setPosition(pos.x + 0.5D, pos.y + 0.25D, pos.z + 0.5D);
		noClip = true;
		height = 0.0001F;
		width = 0.0001F;
		OCCUPIED.put(pos, this);
	}

	@Override
	public void onUpdate()
	{
		if(worldObj.isRemote)
		{
			if(Minecraft.getMinecraft().gameSettings.keyBindSneak.getIsKeyPressed())
			{
				setDead();
				EntitySit.OCCUPIED.remove(new BlockPos((int)posX, (int)posY, (int)posZ));
			}
		}
	}

	@Override
	protected void entityInit(){}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound){}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound){}
}