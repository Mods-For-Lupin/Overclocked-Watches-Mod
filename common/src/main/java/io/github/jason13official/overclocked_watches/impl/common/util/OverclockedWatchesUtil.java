package io.github.jason13official.overclocked_watches.impl.common.util;

import io.github.jason13official.overclocked_watches.Constants;
import io.github.jason13official.overclocked_watches.api.common.data.CoolDownRecord;
import io.github.jason13official.overclocked_watches.api.common.data.IEntityDataSaver;
import io.github.jason13official.overclocked_watches.api.common.data.IItemCooldowns;
import io.github.jason13official.overclocked_watches.impl.common.item.WatchTier;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModDataComponents;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModParticles;
import io.github.jason13official.overclocked_watches.platform.Services;
import java.util.Objects;
import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class OverclockedWatchesUtil {

  public static final String PERSISTENT_DATA_TAG = "OverclockedWatches";

  public static void loadCooldowns(CompoundTag data, Player player) {
    if (Services.PLATFORM.getPlatformName().equalsIgnoreCase("fabric")) {
      data = ((IEntityDataSaver) player).overclocked_watches$getPersistentData();
    }
    if (data.contains(PERSISTENT_DATA_TAG)) {
      ListTag cooldowns = data.getList(PERSISTENT_DATA_TAG, Tag.TAG_COMPOUND);
      cooldowns.forEach((tag) -> {
        if (tag instanceof CompoundTag compoundTag) {
          try {
            ResourceLocation rl = ResourceLocation.parse(compoundTag.getString("item"));
            Item item = Services.PLATFORM.getItemFromRL(rl);
            if (item != (ModItems.GOLDEN_WATCH) && item != (ModItems.DIAMOND_WATCH) && item != (ModItems.NETHERITE_WATCH)) {
              return;
            }
            player.getCooldowns().removeCooldown(item);
            ((IItemCooldowns) player.getCooldowns()).overclocked_watches$addCoolDown(new CoolDownRecord(item, compoundTag.getInt("remain"), compoundTag.getInt("total")));
          } catch (ResourceLocationException e) {
            Constants.LOG.error("Failed to parse item, that's weird.", e);
          }

        }
      });
      if (Services.PLATFORM.isDevelopmentEnvironment()) {
        Constants.LOG.info("[OverclockedWatches] loaded {} cooldown(s) for {}", cooldowns.size(), player.getGameProfile().getName());
      }
    }
  }

  public static void saveCooldowns(CompoundTag tag, Player player) {
    ListTag cooldowns = new ListTag();
    ((IItemCooldowns) player.getCooldowns()).overclocked_watches$getCooldownTicks().forEach((r) -> {
      CompoundTag cooldown = new CompoundTag();
      cooldown.putString("item", Objects.requireNonNull(Services.PLATFORM.getRLFromItem(r.item())).toString());
      cooldown.putInt("remain", r.remain());
      cooldown.putInt("total", r.total());
      cooldowns.add(cooldown);
    });
    tag.put(PERSISTENT_DATA_TAG, cooldowns);
    if (Services.PLATFORM.isDevelopmentEnvironment()) {
      Constants.LOG.info("[OverclockedWatches] saved {} cooldown(s) for {}", cooldowns.size(), player.getGameProfile().getName());
    }
  }

  public static void copyCooldowns(Player oldPlayer, Player newPlayer) {
    CompoundTag temp = new CompoundTag();
    saveCooldowns(temp, oldPlayer);
    loadCooldowns(temp, newPlayer);
  }

  public static void addGrowthParticles(WatchTier tier, ServerLevel level, BlockPos blockPos, int particleCount) {
    for (int i = 0; i < particleCount; ++i) {
      level.sendParticles(ModParticles.getGrowthParticle(tier), ((double) blockPos.getX()) + level.random.nextDouble(), ((double) blockPos.getY()) + 0.5D,
          ((double) blockPos.getZ()) + level.random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 0.2D);
    }
  }

  public static boolean consumeCharge(ItemStack itemInHand) {
    int charges = itemInHand.getOrDefault(ModDataComponents.CHARGES, 0);
    if (charges == 0) {
      return false;
    }
    itemInHand.set(ModDataComponents.CHARGES, charges - 1);
    return true;
  }
}