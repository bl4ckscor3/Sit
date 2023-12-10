package bl4ckscor3.mod.sit;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import snownee.snow.CoreModule;

public class SnowRealMagicSupport {
	private SnowRealMagicSupport() {}

	public static boolean sitDown(Player player, Level level, BlockPos pos, BlockState state) {
		if (state.is(CoreModule.SLAB.get())) {
			SitUtil.sitDown(player, level, pos, 0.3D);
			return true;
		}

		return false;
	}
}
