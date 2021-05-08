package bl4ckscor3.mod.sit.mixin;

import bl4ckscor3.mod.sit.Sit;
import bl4ckscor3.mod.sit.SitEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin
{
	@Shadow
	private ClientWorld world;

	@Inject(method="onEntitySpawn", at=@At(value="HEAD", shift=Shift.AFTER), cancellable=true)
	public void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo callbackInfo) 
	{
		if(packet.getEntityTypeId() == Sit.SIT_ENTITY_TYPE) 
		{
			SitEntity sitEntity = new SitEntity(world);
			double x = packet.getX();
			double y = packet.getY();
			double z = packet.getZ();
			int id = packet.getId();

			sitEntity.updatePosition(x, y, z);
			sitEntity.updateTrackedPosition(x, y, z);
			sitEntity.refreshPositionAfterTeleport(x, y, z);
			sitEntity.pitch = (float)(packet.getPitch() * 360) / 256.0F;
			sitEntity.yaw = (float)(packet.getYaw() * 360) / 256.0F;
			sitEntity.setEntityId(id);
			sitEntity.setUuid(packet.getUuid());
			world.addEntity(id, sitEntity);
			callbackInfo.cancel(); //return;
		}
	}
}
