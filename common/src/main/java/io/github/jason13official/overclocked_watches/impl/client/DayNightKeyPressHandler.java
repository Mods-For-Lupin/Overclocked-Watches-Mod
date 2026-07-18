package io.github.jason13official.overclocked_watches.impl.client;

import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import io.github.jason13official.overclocked_watches.impl.common.util.TimeManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;

public class DayNightKeyPressHandler {

  public static void handle(ClientLevel level, LocalPlayer player) {

    long pAmount = resolvePAmount(player.getMainHandItem().getItem());

    if (!ServerModConfig.useLongTimeDelta) {
      level.setDayTime(level.getDayTime() + pAmount);
    } else {
      TimeManager.CLIENT.addToRemainingTime((int) pAmount);
    }

    player.playSound(SoundEvents.BELL_BLOCK, 1, 1);
    player.playSound(SoundEvents.BELL_RESONATE, 1, 1);
  }

  private static long resolvePAmount(Item mainHandItem) {
    if (mainHandItem == ModItems.NETHERITE_WATCH) {
      return ServerModConfig.netheriteTimeAdvancementTicks;
    }
    if (mainHandItem == ModItems.DIAMOND_WATCH) {
      return ServerModConfig.diamondTimeAdvancementTicks;
    }
    return ServerModConfig.goldenTimeAdvancementTicks;
  }
}
