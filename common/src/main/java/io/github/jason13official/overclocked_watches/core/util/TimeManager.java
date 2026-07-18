package io.github.jason13official.overclocked_watches.core.util;

import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import net.minecraft.client.multiplayer.ClientLevel;
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
        if (shouldOperate()) remainingTime -= Math.toIntExact(ServerModConfig.longTimeDelta);
    }

    public boolean shouldOperate() {
        return remainingTime >= ServerModConfig.longTimeDelta;
    }

    public void operate(ServerLevel level) {
        // System.out.println("operating on server");
        level.setDayTime((level.getDayTime() + ServerModConfig.longTimeDelta) % 24_000L);
        decrementRemainingTime();
    }

    public void operate(ClientLevel level) {
        // System.out.println("operating on client");
        level.setDayTime((level.getDayTime() + ServerModConfig.longTimeDelta) % 24_000L);
        decrementRemainingTime();
    }
}
