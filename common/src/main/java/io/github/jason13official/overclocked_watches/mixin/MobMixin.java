package io.github.jason13official.overclocked_watches.mixin;

import io.github.jason13official.overclocked_watches.Constants;
import io.github.jason13official.overclocked_watches.impl.common.item.WatchTier;
import io.github.jason13official.overclocked_watches.impl.common.util.OverclockedWatchesUtil;
import io.github.jason13official.overclocked_watches.platform.Services;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobMixin {

  @Inject(method = "tick", at = @At("TAIL"))
  private void injected_$_onTick(CallbackInfo callbackInfo) {

    Mob instanceMob = (Mob) (Object) this;
    Level instanceLevel = instanceMob.level();
    if (!(instanceMob instanceof AgeableMob mob) || !(instanceLevel instanceof ServerLevel level)) {
      return;
    }

    if (!(level.getGameTime() % 20 == 0) || mob.age >= 0) {
      return;
    }

    // we are operating on the server, once per second, on a baby mob

    final AABB SEARCH_AREA = new AABB(mob.blockPosition()).inflate(4, 1, 4);
    Iterable<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, SEARCH_AREA);

    final AtomicBoolean FOUND_GOLDEN_WATCH = new AtomicBoolean(false);
    final AtomicBoolean FOUND_DIAMOND_WATCH = new AtomicBoolean(false);
    final AtomicBoolean FOUND_NETHERITE_WATCH = new AtomicBoolean(false);
    // highest tier of watch found so far, wear applies independently per equipped tier
    final WatchTier[] tiersHighestFirst = {WatchTier.NETHERITE, WatchTier.DIAMOND, WatchTier.GOLDEN};
    nearbyPlayers.forEach(player -> {
      for (WatchTier tier : tiersHighestFirst) {
        if (!Services.PLATFORM.playerHasWatchEquipped(player, tier)) {
          continue;
        }

        switch (tier) {
          case NETHERITE -> FOUND_NETHERITE_WATCH.set(true);
          case DIAMOND -> FOUND_DIAMOND_WATCH.set(true);
          case GOLDEN -> FOUND_GOLDEN_WATCH.set(true);
        }

        if (player.getRandom().nextInt(0, 20) == 1) {
          ItemStack equippedWatch = Services.PLATFORM.getEquippedWatch(player, tier);
          equippedWatch.hurtAndBreak(1, level, (ServerPlayer) player, item -> {});
        }
      }
    });

    if (!(FOUND_GOLDEN_WATCH.get() || FOUND_DIAMOND_WATCH.get() || FOUND_NETHERITE_WATCH.get())) {
      return;
    }

    // execute the highest tier of watch first
    if (FOUND_NETHERITE_WATCH.get()) {
      mob.ageUp(Constants.AGE_PROGRESSION_NETHERITE);
      OverclockedWatchesUtil.addGrowthParticles(WatchTier.NETHERITE, level, mob.blockPosition(), 8);
    } else if (FOUND_DIAMOND_WATCH.get()) {
      mob.ageUp(Constants.AGE_PROGRESSION_DIAMOND);
      OverclockedWatchesUtil.addGrowthParticles(WatchTier.DIAMOND, level, mob.blockPosition(), 8);
    } else if (FOUND_GOLDEN_WATCH.get()) {
      mob.ageUp(Constants.AGE_PROGRESSION_GOLD);
      OverclockedWatchesUtil.addGrowthParticles(WatchTier.GOLDEN, level, mob.blockPosition(), 8);
    }
  }
}
