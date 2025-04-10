package com.cursee.overclocked_watches.mixin;

import com.cursee.overclocked_watches.Constants;
import com.cursee.overclocked_watches.core.util.OverclockedWatchesUtil;
import com.cursee.overclocked_watches.platform.Services;
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

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(Mob.class)
public class FabricMobMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void injected_$_onTick(CallbackInfo callbackInfo) {

        Mob instanceMob = (Mob) (Object) this;
        Level instanceLevel = instanceMob.level();
        if (!(instanceMob instanceof AgeableMob) || !(instanceLevel instanceof ServerLevel)) return;

        AgeableMob mob = (AgeableMob) instanceMob;
        ServerLevel level = (ServerLevel) instanceLevel;
        if (!(level.getGameTime() % 20 == 0) || mob.age >= 0) return;

        // we are operating on the server, once per second, on a baby mob

        final AABB SEARCH_AREA = new AABB(mob.blockPosition()).inflate(4, 1, 4);
        Iterable<Player> nearbyPlayers = level.getEntitiesOfClass(Player.class, SEARCH_AREA);

        final AtomicBoolean FOUND_GOLDEN_WATCH = new AtomicBoolean(false);
        final AtomicBoolean FOUND_DIAMOND_WATCH = new AtomicBoolean(false);
        final AtomicBoolean FOUND_NETHERITE_WATCH = new AtomicBoolean(false);
        nearbyPlayers.forEach(player -> {
            if (Services.PLATFORM.playerHasGoldenWatchEquipped(player)) FOUND_GOLDEN_WATCH.set(true);
            if (Services.PLATFORM.playerHasDiamondWatchEquipped(player)) FOUND_DIAMOND_WATCH.set(true);
            if (Services.PLATFORM.playerHasNetheriteWatchEquipped(player)) FOUND_NETHERITE_WATCH.set(true);

            if (FOUND_NETHERITE_WATCH.get() && player.getRandom().nextInt(0, 20) == 1) {
                ItemStack equippedWatch = Services.PLATFORM.getEquippedNetheriteWatch(player);
                equippedWatch.hurt(1, player.getRandom(), (ServerPlayer) player);
                if (equippedWatch.getDamageValue() >= equippedWatch.getMaxDamage()) equippedWatch.shrink(1);
            }
            if (FOUND_DIAMOND_WATCH.get() && player.getRandom().nextInt(0, 20) == 1) {
                ItemStack equippedWatch = Services.PLATFORM.getEquippedDiamondWatch(player);
                equippedWatch.hurt(1, player.getRandom(), (ServerPlayer) player);
                if (equippedWatch.getDamageValue() >= equippedWatch.getMaxDamage()) equippedWatch.shrink(1);
            }
            if (FOUND_GOLDEN_WATCH.get() && player.getRandom().nextInt(0, 20) == 1) {
                ItemStack equippedWatch = Services.PLATFORM.getEquippedGoldenWatch(player);
                equippedWatch.hurt(1, player.getRandom(), (ServerPlayer) player);
                if (equippedWatch.getDamageValue() >= equippedWatch.getMaxDamage()) equippedWatch.shrink(1);
            }
        });

        if (!(FOUND_GOLDEN_WATCH.get() || FOUND_DIAMOND_WATCH.get() || FOUND_NETHERITE_WATCH.get())) return;

        // execute the highest tier of watch first
        if (FOUND_NETHERITE_WATCH.get()) {
            mob.ageUp(Constants.AGE_PROGRESSION_NETHERITE);
            OverclockedWatchesUtil.addNetheriteGrowthParticles(level, mob.blockPosition(), 8);
        }
        else if (FOUND_DIAMOND_WATCH.get()) {
            mob.ageUp(Constants.AGE_PROGRESSION_DIAMOND);
            OverclockedWatchesUtil.addDiamondGrowthParticles(level, mob.blockPosition(), 8);
        }
        else if (FOUND_GOLDEN_WATCH.get()) {
            mob.ageUp(Constants.AGE_PROGRESSION_GOLD);
            OverclockedWatchesUtil.addGoldenGrowthParticles(level, mob.blockPosition(), 8);
        }
    }
}
