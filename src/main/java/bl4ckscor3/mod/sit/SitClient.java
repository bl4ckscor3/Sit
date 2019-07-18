package bl4ckscor3.mod.sit;

import java.io.IOException;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.client.network.packet.EntitySpawnS2CPacket;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SitClient implements ClientModInitializer
{
	private static final Identifier SPAWN_PACKET_ID = new Identifier("sit", "spawn");

	@Override
	public void onInitializeClient()
	{
		ClientSidePacketRegistry.INSTANCE.register(SPAWN_PACKET_ID, (ctx, buf) -> {
			EntitySpawnS2CPacket packet = new EntitySpawnS2CPacket();
			Vec3d pos = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());

			try
			{
				packet.read(buf);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}

			ctx.getTaskQueue().execute(() -> {
				World world = ctx.getPlayer().world;

				if(world instanceof ClientWorld)
				{
					Entity entity = packet.getEntityTypeId().create(world);

					entity.setEntityId(packet.getId());
					entity.setUuid(packet.getUuid());
					entity.setPosition(pos.getX(), pos.getY(), pos.getZ());
					entity.pitch = packet.getPitch() * 360 / 256.0F;
					entity.yaw = packet.getYaw() * 360 / 256.0F;
					((ClientWorld)world).addEntity(packet.getId(), entity);
				}
			});
		});
	}

	public static Packet<?> createSpawnPacket(EntitySit sit, Vec3d pos)
	{
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

		buf.writeDouble(pos.getX());
		buf.writeDouble(pos.getY());
		buf.writeDouble(pos.getZ());

		try
		{
			new EntitySpawnS2CPacket(sit).write(buf);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		return new CustomPayloadS2CPacket(SPAWN_PACKET_ID, buf);
	}
}
