package bl4ckscor3.mod.sit;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SitEntity extends Entity {
	public SitEntity(EntityType<? extends SitEntity> type, Level level) {
		super(type, level);
	}

	public SitEntity(Level level) {
		super(Sit.SIT_ENTITY_TYPE, level);
		noPhysics = true;
	}

	@Override
	public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
		if (passenger instanceof Player player) {
			BlockPos pos = SitUtil.getPreviousPlayerPosition(player, this);

			if (pos != null) {
				discard();
				return new Vec3(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D);
			}
		}

		discard();
		return super.getDismountLocationForPassenger(passenger);
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);
		SitUtil.removeSitEntity(level, blockPosition());
	}

	@Override
	protected void defineSynchedData() {}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {}

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {}

	@Override
	public Packet<?> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}
}