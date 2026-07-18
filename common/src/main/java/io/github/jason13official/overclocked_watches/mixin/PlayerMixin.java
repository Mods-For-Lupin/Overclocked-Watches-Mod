package io.github.jason13official.overclocked_watches.mixin;

import io.github.jason13official.overclocked_watches.impl.common.item.WatchTier;
import io.github.jason13official.overclocked_watches.impl.common.util.OverclockedWatchesUtil;
import io.github.jason13official.overclocked_watches.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PlayerMixin {

  @Unique
  private static void unique_$_forceGrowth(CropBlock crop, BlockState state, ServerLevel level, BlockPos pos) {
    if (level.getRawBrightness(pos, 0) >= 9) {
      int currentAge = crop.getAge(state);
      if (currentAge < crop.getMaxAge()) {
        level.setBlock(pos, crop.getStateForAge(currentAge + 1), 2);
      }
    }
  }

  @Inject(method = "tick()V", at = @At("TAIL"))
  private void injected_$_onTick(CallbackInfo ci) {

    Player instancePlayer = (Player) (Object) this;

    if (!(instancePlayer instanceof ServerPlayer player)) {
      return;
    }

    ServerLevel level = (ServerLevel) player.level();
    if (!(level.getGameTime() % 20 == 0)) {
      return;
    }

    final boolean foundGolden = Services.PLATFORM.playerHasWatchEquipped(player, WatchTier.GOLDEN);
    final boolean foundDiamond = Services.PLATFORM.playerHasWatchEquipped(player, WatchTier.DIAMOND);
    final boolean foundNetherite = Services.PLATFORM.playerHasWatchEquipped(player, WatchTier.NETHERITE);

    if (!(foundGolden || foundDiamond || foundNetherite)) {
      return;
    }

    final AABB SEARCH_AREA = new AABB(player.blockPosition()).inflate(4, 0, 4);
    final Iterable<BlockPos> SEARCH_POSITIONS = BlockPos.betweenClosed((int) SEARCH_AREA.minX, (int) SEARCH_AREA.minY, (int) SEARCH_AREA.minZ, (int) SEARCH_AREA.maxX, (int) SEARCH_AREA.maxY,
        (int) SEARCH_AREA.maxZ);
    SEARCH_POSITIONS.forEach(blockPos -> {

      final BlockState blockState = level.getBlockState(blockPos);
      if (!(blockState.getBlock() instanceof CropBlock crop)) {
        return;
      }

      if (crop.isMaxAge(blockState)) {
        return;
      }

      // highest tier wins
      if (foundNetherite) {
        for (int i = 0; i < 4; i++) {
          unique_$_forceGrowth(crop, blockState, level, blockPos);
        }
        OverclockedWatchesUtil.addGrowthParticles(WatchTier.NETHERITE, level, blockPos, 8);
      } else if (foundDiamond) {
        if (level.random.nextBoolean()) {
          unique_$_forceGrowth(crop, blockState, level, blockPos);
        }
        OverclockedWatchesUtil.addGrowthParticles(WatchTier.DIAMOND, level, blockPos, 8);
      } else if (foundGolden) {
        if (level.random.nextInt(10) == 1) {
          unique_$_forceGrowth(crop, blockState, level, blockPos);
        }
        OverclockedWatchesUtil.addGrowthParticles(WatchTier.GOLDEN, level, blockPos, 8);
      }
    });

    // wear applies independently per equipped tier, highest tier first
    for (WatchTier tier : new WatchTier[]{WatchTier.NETHERITE, WatchTier.DIAMOND, WatchTier.GOLDEN}) {
      boolean found = switch (tier) {
        case NETHERITE -> foundNetherite;
        case DIAMOND -> foundDiamond;
        case GOLDEN -> foundGolden;
      };
      if (!found || player.getRandom().nextInt(0, 20) != 1) {
        continue;
      }

      ItemStack equippedWatch = Services.PLATFORM.getEquippedWatch(player, tier);
      equippedWatch.hurtAndBreak(1, level, player, item -> {});
    }
  }
}
