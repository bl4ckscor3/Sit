package bl4ckscor3.mod.sit;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
			Vec3 resetPosition = SitUtil.getPreviousPlayerPosition(player, this);

			if (resetPosition != null) {
				discard();
				return resetPosition;
			}
		}

		discard();
		return super.getDismountLocationForPassenger(passenger);
	}

	@Override
	public void remove(RemovalReason reason) {
		super.remove(reason);
		SitUtil.removeSitEntity(level(), blockPosition());
	}

	@Override
	protected void defineSynchedData(Builder builder) {}

	@Override
	public void readAdditionalSaveData(ValueInput nbt) {}

	@Override
	public void addAdditionalSaveData(ValueOutput nbt) {}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
		return new ClientboundAddEntityPacket(this, serverEntity);
	}

	@Override
	public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
		return false;
	}

	@Override
	public boolean shouldRender(double x, double y, double z) {
		return false;
	}
}