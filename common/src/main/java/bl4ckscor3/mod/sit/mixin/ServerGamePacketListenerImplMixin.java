package bl4ckscor3.mod.sit.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
	@WrapOperation(method = "handleUseItemOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;sendBuildLimitMessage(ZI)V", ordinal = 5))
	public void makeTheGameNotLie(ServerPlayer instance, boolean isTooHigh, int limit, Operation<Void> original) {
		//No implementation to effectively remove the call to sendBuildLimitMessage
	}
}
