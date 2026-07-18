package io.github.jason13official.overclocked_watches.impl.common;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import io.github.jason13official.overclocked_watches.Constants;
import io.github.jason13official.overclocked_watches.impl.client.ClientModConfig;
import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig.ConfigGetterSetter;
import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig.TierConfig;
import io.github.jason13official.overclocked_watches.impl.common.item.WatchTier;
import io.github.jason13official.overclocked_watches.platform.Services;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

public class ModConfigIO {

  // TODO translate old config file to new format (keys were in camelCaseLikeThis, compared to current snake_case_like_this)
  private static final String OLD_CONFIG_FILE = Constants.MOD_ID + "-common.toml";

  public static void load(Path configDir) {

    File configDirectory = new File(configDir.toUri());
    if (!configDirectory.isDirectory() && !configDirectory.mkdirs()) {
      Constants.LOG.info("failed to get or create config directory");
      return;
    }

    Config.setInsertionOrderPreserved(true);
    loadServerConfig(configDir, Constants.MOD_ID + "-server.toml");
    if (Services.PLATFORM.isClientSide()) {
      loadClientConfig(configDir, Constants.MOD_ID + "-client.toml");
    }
  }

  private static void loadServerConfig(Path configDir, String filename) {

    Path configFilepath = configDir.resolve(filename);
    File configFile = new File(configFilepath.toUri());

    try (CommentedFileConfig config = CommentedFileConfig.builder(configFile).build()) {

      if (Files.exists(configFilepath)) {

        // load existing config into memory
        config.load();

        // make a copy of the old config file
        Files.copy(configFilepath, configDir.resolve(filename + ".OLD"), StandardCopyOption.REPLACE_EXISTING);

        // delete the old config file after loading it into memory and making a .OLD copy
        if (configFile.delete()) {
          Constants.LOG.info("Removed old config file, overwriting with loaded values.");
        }
      }

      loadBoolean(config, ServerModConfig.DAY_NIGHT_CYCLE_ALLOWED, " Whether watches are allowed to advance the day-night cycle. RELOADABLE WITH /reload");
      loadBoolean(config, ServerModConfig.USE_LONG_TIME_DELTA, " Whether time advances gradually over multiple ticks instead of instantly. RELOADABLE WITH /reload");
      loadLong(config, ServerModConfig.LONG_TIME_DELTA, " Ticks of time advanced per server tick while use_long_time_delta is enabled. RELOADABLE WITH /reload");

      for (WatchTier tier : WatchTier.values()) {

        TierConfig tierConfig = ServerModConfig.get(tier);
        String tierName = tier.name().toLowerCase(Locale.ROOT);

        loadLong(config, tierConfig.durability(), " Max durability of the " + tierName + " watch. RELOADABLE WITH /reload");
        loadLong(config, tierConfig.charges(), " Number of charges the " + tierName + " watch holds before it must recharge. RELOADABLE WITH /reload");
        loadLong(config, tierConfig.cooldownMinutes(), " Cooldown, in minutes, before the " + tierName + " watch can be used again. RELOADABLE WITH /reload");
        loadLong(config, tierConfig.timeAdvancementTicks(), " Ticks of time advanced per use of the " + tierName + " watch. RELOADABLE WITH /reload");
      }

      config.save();
    } catch (Exception e) {
      Constants.LOG.error("failed to read or write config file", e);
    }
  }

  private static void loadBoolean(CommentedFileConfig config, ConfigGetterSetter<Boolean> setting, String comment) {

    setting.set(Boolean.parseBoolean(config.getOrElse(setting.key(), String.valueOf(setting.get().booleanValue()))));
    config.setComment(setting.key(), comment);
    config.set(setting.key(), String.valueOf(setting.get().booleanValue()));
  }

  private static void loadLong(CommentedFileConfig config, ConfigGetterSetter<Long> setting, String comment) {

    setting.set(Long.parseLong(config.getOrElse(setting.key(), String.valueOf(setting.get().longValue()))));
    config.setComment(setting.key(), comment);
    config.set(setting.key(), String.valueOf(setting.get().longValue()));
  }

  private static void loadClientConfig(Path configDir, String filename) {

    Path configFilepath = configDir.resolve(filename);
    File configFile = new File(configFilepath.toUri());

    try (CommentedFileConfig config = CommentedFileConfig.builder(configFile).build()) {

      if (Files.exists(configFilepath)) {

        // load existing config into memory
        config.load();

        // make a copy of the old config file
        Files.copy(configFilepath, configDir.resolve(filename + ".OLD"), StandardCopyOption.REPLACE_EXISTING);

        // delete the old config file after loading it into memory and making a .OLD copy
        if (configFile.delete()) {
          Constants.LOG.info("Removed old client config file, overwriting with loaded values.");
        }
      }

      // getters (loading from config file)
      ClientModConfig.DEFAULT_DAY_NIGHT_KEY.set(Long.parseLong(config.getOrElse(ClientModConfig.DEFAULT_DAY_NIGHT_KEY.key(), String.valueOf(ClientModConfig.DEFAULT_DAY_NIGHT_KEY.get().longValue()))));

      // setters (saving to config file)
      config.setComment(ClientModConfig.DEFAULT_DAY_NIGHT_KEY.key(), " Key-bind for using the watch while not holding it.");
      config.set(ClientModConfig.DEFAULT_DAY_NIGHT_KEY.key(), String.valueOf(ClientModConfig.DEFAULT_DAY_NIGHT_KEY.get().longValue()));

      config.save();
    } catch (Exception e) {
      Constants.LOG.error("failed to read or write config file", e);
    }
  }
}
