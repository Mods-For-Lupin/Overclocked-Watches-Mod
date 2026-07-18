package io.github.jason13official.overclocked_watches.impl.common.network.packet;

import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import io.github.jason13official.overclocked_watches.impl.common.util.OverclockedWatchesUtil;
import io.github.jason13official.overclocked_watches.impl.common.util.TimeManager;
import io.github.jason13official.overclocked_watches.platform.Services;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;

public class DayNightC2SHandler {

  public static final String CHARGES = "charges";

  public static void handle(MinecraftServer server, ServerPlayer player) {

    if (!ServerModConfig.DAY_NIGHT_CYCLE_ALLOWED.get()) {
      return;
    }

    boolean hasNetheriteWatch = Services.PLATFORM.playerHasNetheriteWatchEquipped(player);
    boolean hasDiamondWatch = Services.PLATFORM.playerHasDiamondWatchEquipped(player);
    boolean hasGoldenWatch = Services.PLATFORM.playerHasGoldenWatchEquipped(player);
    if (!Services.PLATFORM.hasAnyWatchEquipped(player)) {
      return;
    }

    ItemCooldowns cooldowns = player.getCooldowns();
    boolean onCooldown = cooldowns.isOnCooldown(ModItems.NETHERITE_WATCH) || cooldowns.isOnCooldown(ModItems.DIAMOND_WATCH) || cooldowns.isOnCooldown(ModItems.GOLDEN_WATCH);
    if (onCooldown) {
      return;
    }

    if (hasNetheriteWatch) {
      ItemStack equippedWatch = Services.PLATFORM.getEquippedNetheriteWatch(player);
      if (!OverclockedWatchesUtil.consumeCharge(equippedWatch)) {
        return;
      }
      addTime(server, ServerModConfig.NETHERITE_TIME_ADVANCEMENT_TICKS.get());
      applyCooldowns(player, 20 * 60 * (int) ServerModConfig.NETHERITE_WATCH_COOLDOWN_MINUTES.get().longValue());
    } else if (hasDiamondWatch) {
      ItemStack watch = Services.PLATFORM.getEquippedDiamondWatch(player);
      if (!OverclockedWatchesUtil.consumeCharge(watch)) {
        return;
      }
      addTime(server, ServerModConfig.DIAMOND_TIME_ADVANCEMENT_TICKS.get());
      applyCooldowns(player, 20 * 60 * (int) ServerModConfig.DIAMOND_WATCH_COOLDOWN_MINUTES.get().longValue());
    } else {
      ItemStack watch = Services.PLATFORM.getEquippedGoldenWatch(player);
      if (!OverclockedWatchesUtil.consumeCharge(watch)) {
        return;
      }
      addTime(server, ServerModConfig.GOLDEN_TIME_ADVANCEMENT_TICKS.get());
      applyCooldowns(player, 20 * 60 * (int) ServerModConfig.GOLDEN_WATCH_COOLDOWN_MINUTES.get().longValue());
    }

    player.serverLevel().playLocalSound(player.position().x, player.position().y, player.position().z, SoundEvents.BELL_RESONATE, SoundSource.AMBIENT, 0.5f, 0.5f, false);
    player.serverLevel().addParticle(ParticleTypes.END_ROD, player.position().x, player.position().y, player.position().z, 0, 0.005, 0);
    player.sendSystemMessage(Component.translatable("magic.overclocked_watches.charge_consumed"));
  }

  public static void applyCooldowns(Player player, int lengthInTicks) {
    player.getCooldowns().addCooldown(ModItems.NETHERITE_WATCH, lengthInTicks);
    player.getCooldowns().addCooldown(ModItems.DIAMOND_WATCH, lengthInTicks);
    player.getCooldowns().addCooldown(ModItems.GOLDEN_WATCH, lengthInTicks);
  }

  public static void addTime(MinecraftServer pSource, long pAmount) {
    for (ServerLevel level : pSource.getAllLevels()) {
      if (!ServerModConfig.USE_LONG_TIME_DELTA.get()) {
        level.setDayTime(level.getDayTime() + pAmount);
      }
    }

    if (ServerModConfig.USE_LONG_TIME_DELTA.get()) {
      TimeManager.SERVER.addToRemainingTime((int) pAmount);
    }
  }
}
