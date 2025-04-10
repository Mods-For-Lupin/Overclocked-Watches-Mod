package com.cursee.overclocked_watches.core.util;

import com.cursee.overclocked_watches.core.CommonConfigValues;
import com.cursee.overclocked_watches.core.registry.ModParticles;
import com.cursee.overclocked_watches.core.world.item.WatchItem;
import com.cursee.overclocked_watches.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

public class OverclockedWatchesUtil {

    public static void addGoldenGrowthParticles(ServerLevel level, BlockPos blockPos, int particleCount) {
        for(int i = 0; i < particleCount; ++i) {
            level.sendParticles(ModParticles.GOLDEN_WATCH_GROWTH, ((double) blockPos.getX()) + level.random.nextDouble(), ((double) blockPos.getY()) + 0.5D, ((double) blockPos.getZ()) + level.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 0.2D);
        }
    }

    public static void addDiamondGrowthParticles(ServerLevel level, BlockPos blockPos, int particleCount) {
        for(int i = 0; i < particleCount; ++i) {
            level.sendParticles(ModParticles.DIAMOND_WATCH_GROWTH, ((double) blockPos.getX()) + level.random.nextDouble(), ((double) blockPos.getY()) + 0.5D, ((double) blockPos.getZ()) + level.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 0.2D);
        }
    }

    public static void addNetheriteGrowthParticles(ServerLevel level, BlockPos blockPos, int particleCount) {
        for(int i = 0; i < particleCount; ++i) {
            level.sendParticles(ModParticles.NETHERITE_WATCH_GROWTH, ((double) blockPos.getX()) + level.random.nextDouble(), ((double) blockPos.getY()) + 0.5D, ((double) blockPos.getZ()) + level.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 0.2D);
        }
    }

    // public static final String TAG_CHARGES = "charges";

    public static boolean handleNetheriteWatchTag(ItemStack itemStack) {

        CompoundTag baseTag = itemStack.getOrCreateTag();
        if (!baseTag.contains(WatchItem.CHARGES)) baseTag.put(WatchItem.CHARGES, IntTag.valueOf((int) CommonConfigValues.netherite_watch_charges));

        int currentCharges = baseTag.getInt(WatchItem.CHARGES);

        if (currentCharges > 0) {
            currentCharges -= 1;
            baseTag.put(WatchItem.CHARGES, IntTag.valueOf(currentCharges));
            return true;
        }

        return false;
    }

    public static boolean handleDiamondWatchTag(ItemStack itemStack) {

        CompoundTag baseTag = itemStack.getOrCreateTag();
        if (!baseTag.contains(WatchItem.CHARGES)) baseTag.put(WatchItem.CHARGES, IntTag.valueOf((int) CommonConfigValues.diamond_watch_charges));

        int currentCharges = baseTag.getInt(WatchItem.CHARGES);

        if (currentCharges > 0) {
            currentCharges -= 1;
            baseTag.put(WatchItem.CHARGES, IntTag.valueOf(currentCharges));
            return true;
        }

        return false;
    }

    public static boolean handleGoldenWatchTag(ItemStack itemStack) {

        CompoundTag baseTag = itemStack.getOrCreateTag();
        if (!baseTag.contains(WatchItem.CHARGES)) baseTag.put(WatchItem.CHARGES, IntTag.valueOf((int) CommonConfigValues.golden_watch_charges));

        int currentCharges = baseTag.getInt(WatchItem.CHARGES);

        if (currentCharges > 0) {
            currentCharges -= 1;
            baseTag.put(WatchItem.CHARGES, IntTag.valueOf(currentCharges));
            return true;
        }

        return false;
    }
}