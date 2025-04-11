package com.cursee.overclocked_watches.core.util;

import com.cursee.overclocked_watches.core.CommonConfigValues;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public class TimeManager {

    public static int remainingTime = 0;

    public static void addToRemainingTime(int value) {
        remainingTime += value;
    }

    public static void decrementRemainingTime() {
        if (shouldOperate()) remainingTime -= Math.toIntExact(CommonConfigValues.long_time_delta);
    }

    public static boolean shouldOperate() {
        return remainingTime >= CommonConfigValues.long_time_delta;
    }

    public static void operate(ServerLevel level) {
        level.setDayTime((level.getDayTime() + CommonConfigValues.long_time_delta) % 24_000L);
        decrementRemainingTime();
    }

    public static void operate(ClientLevel level) {
        level.setDayTime((level.getDayTime() + CommonConfigValues.long_time_delta) % 24_000L);
        decrementRemainingTime();
    }
}
