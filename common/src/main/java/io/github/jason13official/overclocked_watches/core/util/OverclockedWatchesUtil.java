package io.github.jason13official.overclocked_watches.core.util;

import io.github.jason13official.overclocked_watches.Constants;
import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import io.github.jason13official.overclocked_watches.core.registry.ModItems;
import io.github.jason13official.overclocked_watches.core.registry.ModParticles;
import io.github.jason13official.overclocked_watches.core.world.item.WatchItem;
import io.github.jason13official.overclocked_watches.platform.Services;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class OverclockedWatchesUtil {

    public static void loadCooldowns(CompoundTag data, Player player) {
        if (Services.PLATFORM.getPlatformName().equalsIgnoreCase("fabric")) data = ((IEntityDataSaver) player).getPersistentData();
        if (data.contains("ocw.watch_data")) {
            ListTag cooldowns = data.getList("ocw.watch_data", Tag.TAG_COMPOUND);
            cooldowns.forEach((tag) -> {
                if (tag instanceof CompoundTag compoundTag) {
                    try {
                        ResourceLocation rl = new ResourceLocation(compoundTag.getString("item"));
                        Item item = Services.PLATFORM.getItemFromRL(rl);
//                        if (item == null || item.equals(Items.AIR)) {
//                            return;
//                        }
//                        if (!item.equals(ModItems.GOLDEN_WATCH) && !item.equals(ModItems.DIAMOND_WATCH) && !item.equals(ModItems.NETHERITE_WATCH)) {
//                            return;
//                        }
                        if (item != (ModItems.GOLDEN_WATCH) && item != (ModItems.DIAMOND_WATCH) && item != (ModItems.NETHERITE_WATCH)) {
                            return;
                        }
                        player.getCooldowns().removeCooldown(item);
                        ((IItemCooldowns)player.getCooldowns()).persistcd$addCoolDown(new CoolDownRecord(item, compoundTag.getInt("remain"), compoundTag.getInt("total")));
                    } catch (ResourceLocationException e) {
                        Constants.LOG.error("Failed to parse item, that's weird.", e);
                    }

                }
            });
        }
    }

    public static void saveCooldowns(CompoundTag tag, Player player) {
        ListTag cooldowns = new ListTag();
        ((IItemCooldowns)player.getCooldowns()).persistcd$getCooldownTicks().forEach((r) -> {
            CompoundTag cooldown = new CompoundTag();
            cooldown.putString("item", ((ResourceLocation) Objects.requireNonNull(Services.PLATFORM.getRLFromItem(r.item()))).toString());
            cooldown.putInt("remain", r.remain());
            cooldown.putInt("total", r.total());
            cooldowns.add(cooldown);
        });
        tag.put("ocw.watch_data", cooldowns);
    }

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
        if (!baseTag.contains(WatchItem.CHARGES)) baseTag.put(WatchItem.CHARGES, IntTag.valueOf((int) ServerModConfig.netheriteWatchCharges));

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
        if (!baseTag.contains(WatchItem.CHARGES)) baseTag.put(WatchItem.CHARGES, IntTag.valueOf((int) ServerModConfig.diamondWatchCharges));

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
        if (!baseTag.contains(WatchItem.CHARGES)) baseTag.put(WatchItem.CHARGES, IntTag.valueOf((int) ServerModConfig.goldenWatchCharges));

        int currentCharges = baseTag.getInt(WatchItem.CHARGES);

        if (currentCharges > 0) {
            currentCharges -= 1;
            baseTag.put(WatchItem.CHARGES, IntTag.valueOf(currentCharges));
            return true;
        }

        return false;
    }
}