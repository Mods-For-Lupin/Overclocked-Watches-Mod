package io.github.jason13official.overclocked_watches.platform.services;

import io.github.jason13official.overclocked_watches.api.client.renderer.IWatchRenderer;
import io.github.jason13official.overclocked_watches.impl.common.item.WatchTier;
import io.github.jason13official.overclocked_watches.platform.Services;
import java.nio.file.Path;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IPlatformHelper {

  /**
   * Gets the name of the current platform
   *
   * @return The name of the current platform.
   */
  String getPlatformName();

  /**
   * Checks if a mod with the given id is loaded.
   *
   * @param modId The mod to check if it is loaded.
   * @return True if the mod is loaded, false otherwise.
   */
  boolean isModLoaded(String modId);

  /**
   * Check if the game is currently in a development environment.
   *
   * @return True if in a development environment, false otherwise.
   */
  boolean isDevelopmentEnvironment();

  /**
   * Gets the name of the environment type as a string.
   *
   * @return The name of the environment type.
   */
  default String getEnvironmentName() {

    return isDevelopmentEnvironment() ? "development" : "production";
  }

  Path getGameDirectory();

  default Path getConfigDirectory() {

    return getGameDirectory().resolve("config");
  }

  CreativeModeTab.Builder tabBuilder();

  boolean isClientSide();

  <T extends Item> IWatchRenderer getWatchRenderer(T item);

  <T extends Item> void registerWatchRenderer(T item, Supplier<IWatchRenderer> rendererSupplier);

  boolean playerHasWatchEquipped(Player player, WatchTier tier);

  default boolean hasAnyWatchEquipped(Player player) {
    for (WatchTier tier : WatchTier.values()) {
      if (Services.PLATFORM.playerHasWatchEquipped(player, tier)) {
        return true;
      }
    }
    return false;
  }

  ItemStack getEquippedWatch(Player player, WatchTier tier);

  Item getItemFromRL(ResourceLocation rl);

  ResourceLocation getRLFromItem(Item item);

  CompoundTag getPersistentData(Entity entity);
}