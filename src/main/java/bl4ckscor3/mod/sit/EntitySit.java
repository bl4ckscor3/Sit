package bl4ckscor3.mod.sit;

import java.util.HashMap;

import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.minecraft.client.network.packet.EntityPositionS2CPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntitySit extends Entity
{
	public static final HashMap<Vec3d,EntitySit> OCCUPIED = new HashMap<Vec3d,EntitySit>();
	private int ticks = 0;

	public EntitySit(EntityType<? extends EntitySit> type, World world)
	{
		super(type, world);
		noClip = true;
	}

	@Override
	public void tick()
	{
		super.tick();

		if(!hasPassengers())
		{
			EntitySit.OCCUPIED.remove(getPos());
			destroy();
		}

		if(!world.isClient && ticks++ % 60 == 0) //it's shitty handling but idk wtf to do, the position on the client resets after a short amount of time
		{
			ticks = 0;
			PlayerStream.watching(this).forEach(e -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(e, new EntityPositionS2CPacket(this)));
		}
	}

	public void setPosAndSync(Vec3d pos)
	{
		x = pos.getX();
		y = pos.getY();
		z = pos.getZ();
		PlayerStream.watching(this).forEach(e -> ServerSidePacketRegistry.INSTANCE.sendToPlayer(e, new EntityPositionS2CPacket(this)));
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
		return SitClient.createSpawnPacket(this, getPos());
	}

	@Override
	protected boolean canClimb()
	{
		return false;
	}

	@Override
	public boolean collides()
	{
		return false;
	}

	@Override
	public boolean hasNoGravity()
	{
		return true;
	}

	@Override
	public boolean isInvisible()
	{
		return true;
	}
}