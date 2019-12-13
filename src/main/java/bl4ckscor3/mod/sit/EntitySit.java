package bl4ckscor3.mod.sit;

import java.util.Collections;
import java.util.HashMap;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntitySit extends LivingEntity
{
	public static final HashMap<Vec3d,EntitySit> OCCUPIED = new HashMap<Vec3d,EntitySit>();

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
			remove();
		}
	}

	@Override
	public  void initDataTracker()
	{
		super.initDataTracker();
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag) {}

	@Override
	public void writeCustomDataToTag(CompoundTag tag) {}

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

	@Override
	public float getHealth()
	{
		return 100000.0F;
	}

	@Override
	protected boolean canDropLootAndXp()
	{
		return false;
	}

	@Override
	public boolean canHaveStatusEffect(StatusEffectInstance statusEffectInstance)
	{
		return false;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return null;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return null;
	}

	@Override
	public void equipStack(EquipmentSlot equipmentSlot, ItemStack itemStack) {}

	@Override
	public Iterable<ItemStack> getArmorItems()
	{
		return Collections.EMPTY_LIST;
	}

	@Override
	public ItemStack getEquippedStack(EquipmentSlot equipmentSlot)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public Arm getMainArm()
	{
		return Arm.RIGHT;
	}
}