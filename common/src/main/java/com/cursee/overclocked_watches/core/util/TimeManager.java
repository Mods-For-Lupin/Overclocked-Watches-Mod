package com.cursee.overclocked_watches.core.util;

import com.cursee.overclocked_watches.core.CommonConfigValues;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public class TimeManager {

    public static TimeManager SERVER = null;
    public static TimeManager CLIENT = null;

    private int remainingTime = 0;

    public int getRemainingTime() {
        return remainingTime;
    }

    public void addToRemainingTime(int value) {
        remainingTime += value;
    }

    public void decrementRemainingTime() {
        if (shouldOperate()) remainingTime -= Math.toIntExact(CommonConfigValues.long_time_delta);
    }

    public boolean shouldOperate() {
        return remainingTime >= CommonConfigValues.long_time_delta;
    }

    public void operate(ServerLevel level) {
        // System.out.println("operating on server");
        level.setDayTime((level.getDayTime() + CommonConfigValues.long_time_delta) % 24_000L);
        decrementRemainingTime();
    }

    public void operate(ClientLevel level) {
        // System.out.println("operating on client");
        level.setDayTime((level.getDayTime() + CommonConfigValues.long_time_delta) % 24_000L);
        decrementRemainingTime();
    }
}
