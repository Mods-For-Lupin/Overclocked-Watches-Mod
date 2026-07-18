package io.github.jason13official.overclocked_watches.impl.common.network.packet;

import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import io.github.jason13official.overclocked_watches.impl.common.util.TimeManager;
import io.github.jason13official.overclocked_watches.platform.Services;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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

    if (!ServerModConfig.dayNightCycleAllowed) {
      return;
    }

    boolean hasNetheriteWatch = Services.PLATFORM.playerHasNetheriteWatchEquipped(player);
    boolean hasDiamondWatch = Services.PLATFORM.playerHasDiamondWatchEquipped(player);
    boolean hasGoldenWatch = Services.PLATFORM.playerHasGoldenWatchEquipped(player);
    if (!(hasNetheriteWatch || hasDiamondWatch || hasGoldenWatch)) {
      return;
    }

    ItemCooldowns cooldowns = player.getCooldowns();
    boolean onCooldown = cooldowns.isOnCooldown(ModItems.NETHERITE_WATCH) || cooldowns.isOnCooldown(ModItems.DIAMOND_WATCH) || cooldowns.isOnCooldown(ModItems.GOLDEN_WATCH);
    if (onCooldown) {
      return;
    }

    if (hasNetheriteWatch) {
      ItemStack watch = Services.PLATFORM.getEquippedNetheriteWatch(player);
      if (!consumeCharge(watch)) {
        return;
      }
      addTime(server, ServerModConfig.netheriteTimeAdvancementTicks);
      applyCooldowns(player, 20 * 60 * (int) ServerModConfig.netheriteWatchCooldownMinutes);
    } else if (hasDiamondWatch) {
      ItemStack watch = Services.PLATFORM.getEquippedDiamondWatch(player);
      if (!consumeCharge(watch)) {
        return;
      }
      addTime(server, ServerModConfig.diamondTimeAdvancementTicks);
      applyCooldowns(player, 20 * 60 * (int) ServerModConfig.diamondWatchCooldownMinutes);
    } else {
      ItemStack watch = Services.PLATFORM.getEquippedGoldenWatch(player);
      if (!consumeCharge(watch)) {
        return;
      }
      addTime(server, ServerModConfig.goldenTimeAdvancementTicks);
      applyCooldowns(player, 20 * 60 * (int) ServerModConfig.goldenWatchCooldownMinutes);
    }

    player.serverLevel().playLocalSound(player.position().x, player.position().y, player.position().z, SoundEvents.BELL_RESONATE, SoundSource.AMBIENT, 0.5f, 0.5f, false);
    player.serverLevel().addParticle(ParticleTypes.END_ROD, player.position().x, player.position().y, player.position().z, 0, 0.005, 0);
    player.sendSystemMessage(Component.translatable("magic.overclocked_watches.charge_consumed"));
  }

  private static boolean consumeCharge(ItemStack watch) {
    CompoundTag data = watch.getOrCreateTag();
    if (data.getInt(CHARGES) == 0) {
      return false;
    }
    int newCharges = data.getInt(CHARGES) - 1;
    data.remove(CHARGES);
    data.putInt(CHARGES, newCharges);
    watch.save(data);
    return true;
  }

  public static void applyCooldowns(Player player, int lengthInTicks) {
    player.getCooldowns().addCooldown(ModItems.NETHERITE_WATCH, lengthInTicks);
    player.getCooldowns().addCooldown(ModItems.DIAMOND_WATCH, lengthInTicks);
    player.getCooldowns().addCooldown(ModItems.GOLDEN_WATCH, lengthInTicks);
  }

  public static void addTime(MinecraftServer pSource, long pAmount) {
    for (ServerLevel level : pSource.getAllLevels()) {
      if (!ServerModConfig.useLongTimeDelta) {
        level.setDayTime(level.getDayTime() + pAmount);
      }
    }

    if (ServerModConfig.useLongTimeDelta) {
      TimeManager.SERVER.addToRemainingTime((int) pAmount);
    }
  }
}
