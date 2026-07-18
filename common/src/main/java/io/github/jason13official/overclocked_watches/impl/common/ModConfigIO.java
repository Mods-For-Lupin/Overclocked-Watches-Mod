package io.github.jason13official.overclocked_watches.impl.common;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import io.github.jason13official.overclocked_watches.Constants;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ModConfigIO {

  // TODO translate old config file to new format
  private static final String OLD_CONFIG_FILE = Constants.MOD_ID + "-common.toml";

  public static void load(Path configDir) {

    File configDirectory = new File(configDir.toUri());
    if (!configDirectory.isDirectory() && !configDirectory.mkdirs()) {
      Constants.LOG.info("failed to get or create config directory");
      return;
    }

    Config.setInsertionOrderPreserved(true);
    loadServerConfig(configDir, Constants.MOD_ID + "-server.toml");
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

      // getters (loading from config file)
      ServerModConfig.DAY_NIGHT_CYCLE_ALLOWED.setter().accept(Boolean.parseBoolean(config.getOrElse(ServerModConfig.DAY_NIGHT_CYCLE_ALLOWED.key(), String.valueOf(ServerModConfig.DAY_NIGHT_CYCLE_ALLOWED.getter().get().booleanValue()))));
      ServerModConfig.DEFAULT_DAY_NIGHT_KEY.setter().accept(Long.parseLong(config.getOrElse(ServerModConfig.DEFAULT_DAY_NIGHT_KEY.key(), String.valueOf(ServerModConfig.DEFAULT_DAY_NIGHT_KEY.getter().get().longValue()))));
      ServerModConfig.USE_LONG_TIME_DELTA.setter().accept(Boolean.parseBoolean(config.getOrElse(ServerModConfig.USE_LONG_TIME_DELTA.key(), String.valueOf(ServerModConfig.USE_LONG_TIME_DELTA.getter().get().booleanValue()))));
      ServerModConfig.LONG_TIME_DELTA.setter().accept(Long.parseLong(config.getOrElse(ServerModConfig.LONG_TIME_DELTA.key(), String.valueOf(ServerModConfig.LONG_TIME_DELTA.getter().get().longValue()))));
      ServerModConfig.GOLDEN_WATCH_DURABILITY.setter().accept(Long.parseLong(config.getOrElse(ServerModConfig.GOLDEN_WATCH_DURABILITY.key(), String.valueOf(ServerModConfig.GOLDEN_WATCH_DURABILITY.getter().get().longValue()))));
      ServerModConfig.DIAMOND_WATCH_DURABILITY.setter().accept(Long.parseLong(config.getOrElse(ServerModConfig.DIAMOND_WATCH_DURABILITY.key(), String.valueOf(ServerModConfig.DIAMOND_WATCH_DURABILITY.getter().get().longValue()))));
      ServerModConfig.NETHERITE_WATCH_DURABILITY.setter().accept(Long.parseLong(config.getOrElse(ServerModConfig.NETHERITE_WATCH_DURABILITY.key(), String.valueOf(ServerModConfig.NETHERITE_WATCH_DURABILITY.getter().get().longValue()))));
      ServerModConfig.GOLDEN_WATCH_CHARGES.setter().accept(Long.parseLong(config.getOrElse(ServerModConfig.GOLDEN_WATCH_CHARGES.key(), String.valueOf(ServerModConfig.GOLDEN_WATCH_CHARGES.getter().get().longValue()))));
      ServerModConfig.DIAMOND_WATCH_CHARGES.setter().accept(Long.parseLong(config.getOrElse(ServerModConfig.DIAMOND_WATCH_CHARGES.key(), String.valueOf(ServerModConfig.DIAMOND_WATCH_CHARGES.getter().get().longValue()))));
      ServerModConfig.NETHERITE_WATCH_CHARGES.setter().accept(Long.parseLong(config.getOrElse(ServerModConfig.NETHERITE_WATCH_CHARGES.key(), String.valueOf(ServerModConfig.NETHERITE_WATCH_CHARGES.getter().get().longValue()))));
      ServerModConfig.GOLDEN_WATCH_COOLDOWN_MINUTES.setter().accept(Long.parseLong(config.getOrElse(ServerModConfig.GOLDEN_WATCH_COOLDOWN_MINUTES.key(), String.valueOf(ServerModConfig.GOLDEN_WATCH_COOLDOWN_MINUTES.getter().get().longValue()))));
      ServerModConfig.DIAMOND_WATCH_COOLDOWN_MINUTES.setter().accept(Long.parseLong(config.getOrElse(ServerModConfig.DIAMOND_WATCH_COOLDOWN_MINUTES.key(), String.valueOf(ServerModConfig.DIAMOND_WATCH_COOLDOWN_MINUTES.getter().get().longValue()))));
      ServerModConfig.NETHERITE_WATCH_COOLDOWN_MINUTES.setter().accept(Long.parseLong(config.getOrElse(ServerModConfig.NETHERITE_WATCH_COOLDOWN_MINUTES.key(), String.valueOf(ServerModConfig.NETHERITE_WATCH_COOLDOWN_MINUTES.getter().get().longValue()))));
      ServerModConfig.GOLDEN_TIME_ADVANCEMENT_TICKS.setter().accept(Long.parseLong(config.getOrElse(ServerModConfig.GOLDEN_TIME_ADVANCEMENT_TICKS.key(), String.valueOf(ServerModConfig.GOLDEN_TIME_ADVANCEMENT_TICKS.getter().get().longValue()))));
      ServerModConfig.DIAMOND_TIME_ADVANCEMENT_TICKS.setter().accept(Long.parseLong(config.getOrElse(ServerModConfig.DIAMOND_TIME_ADVANCEMENT_TICKS.key(), String.valueOf(ServerModConfig.DIAMOND_TIME_ADVANCEMENT_TICKS.getter().get().longValue()))));
      ServerModConfig.NETHERITE_TIME_ADVANCEMENT_TICKS.setter().accept(Long.parseLong(config.getOrElse(ServerModConfig.NETHERITE_TIME_ADVANCEMENT_TICKS.key(), String.valueOf(ServerModConfig.NETHERITE_TIME_ADVANCEMENT_TICKS.getter().get().longValue()))));

      // setters (saving to config file)
      config.setComment(ServerModConfig.DAY_NIGHT_CYCLE_ALLOWED.key(), " Range of chunks that the player cannot remain in. RELOADABLE WITH /reload");
      config.setComment(ServerModConfig.DEFAULT_DAY_NIGHT_KEY.key(), " Range of chunks that the player cannot remain in. RELOADABLE WITH /reload");
      config.setComment(ServerModConfig.USE_LONG_TIME_DELTA.key(), " Range of chunks that the player cannot remain in. RELOADABLE WITH /reload");
      config.setComment(ServerModConfig.LONG_TIME_DELTA.key(), " Range of chunks that the player cannot remain in. RELOADABLE WITH /reload");
      config.setComment(ServerModConfig.GOLDEN_WATCH_DURABILITY.key(), " Range of chunks that the player cannot remain in. RELOADABLE WITH /reload");
      config.setComment(ServerModConfig.DIAMOND_WATCH_DURABILITY.key(), " Range of chunks that the player cannot remain in. RELOADABLE WITH /reload");
      config.setComment(ServerModConfig.NETHERITE_WATCH_DURABILITY.key(), " Range of chunks that the player cannot remain in. RELOADABLE WITH /reload");
      config.setComment(ServerModConfig.GOLDEN_WATCH_CHARGES.key(), " Range of chunks that the player cannot remain in. RELOADABLE WITH /reload");
      config.setComment(ServerModConfig.DIAMOND_WATCH_CHARGES.key(), " Range of chunks that the player cannot remain in. RELOADABLE WITH /reload");
      config.setComment(ServerModConfig.NETHERITE_WATCH_CHARGES.key(), " Range of chunks that the player cannot remain in. RELOADABLE WITH /reload");
      config.setComment(ServerModConfig.GOLDEN_WATCH_COOLDOWN_MINUTES.key(), " Range of chunks that the player cannot remain in. RELOADABLE WITH /reload");
      config.setComment(ServerModConfig.DIAMOND_WATCH_COOLDOWN_MINUTES.key(), " Range of chunks that the player cannot remain in. RELOADABLE WITH /reload");
      config.setComment(ServerModConfig.NETHERITE_WATCH_COOLDOWN_MINUTES.key(), " Range of chunks that the player cannot remain in. RELOADABLE WITH /reload");
      config.setComment(ServerModConfig.GOLDEN_TIME_ADVANCEMENT_TICKS.key(), " Range of chunks that the player cannot remain in. RELOADABLE WITH /reload");
      config.setComment(ServerModConfig.DIAMOND_TIME_ADVANCEMENT_TICKS.key(), " Range of chunks that the player cannot remain in. RELOADABLE WITH /reload");
      config.setComment(ServerModConfig.NETHERITE_TIME_ADVANCEMENT_TICKS.key(), " Range of chunks that the player cannot remain in. RELOADABLE WITH /reload");

      config.set(ServerModConfig.DAY_NIGHT_CYCLE_ALLOWED.key(), String.valueOf(ServerModConfig.DAY_NIGHT_CYCLE_ALLOWED.getter().get().booleanValue()));
      config.set(ServerModConfig.DEFAULT_DAY_NIGHT_KEY.key(), String.valueOf(ServerModConfig.DEFAULT_DAY_NIGHT_KEY.getter().get().longValue()));
      config.set(ServerModConfig.USE_LONG_TIME_DELTA.key(), String.valueOf(ServerModConfig.USE_LONG_TIME_DELTA.getter().get().booleanValue()));
      config.set(ServerModConfig.LONG_TIME_DELTA.key(), String.valueOf(ServerModConfig.LONG_TIME_DELTA.getter().get().longValue()));
      config.set(ServerModConfig.GOLDEN_WATCH_DURABILITY.key(), String.valueOf(ServerModConfig.GOLDEN_WATCH_DURABILITY.getter().get().longValue()));
      config.set(ServerModConfig.DIAMOND_WATCH_DURABILITY.key(), String.valueOf(ServerModConfig.DIAMOND_WATCH_DURABILITY.getter().get().longValue()));
      config.set(ServerModConfig.NETHERITE_WATCH_DURABILITY.key(), String.valueOf(ServerModConfig.NETHERITE_WATCH_DURABILITY.getter().get().longValue()));
      config.set(ServerModConfig.GOLDEN_WATCH_CHARGES.key(), String.valueOf(ServerModConfig.GOLDEN_WATCH_CHARGES.getter().get().longValue()));
      config.set(ServerModConfig.DIAMOND_WATCH_CHARGES.key(), String.valueOf(ServerModConfig.DIAMOND_WATCH_CHARGES.getter().get().longValue()));
      config.set(ServerModConfig.NETHERITE_WATCH_CHARGES.key(), String.valueOf(ServerModConfig.NETHERITE_WATCH_CHARGES.getter().get().longValue()));
      config.set(ServerModConfig.GOLDEN_WATCH_COOLDOWN_MINUTES.key(), String.valueOf(ServerModConfig.GOLDEN_WATCH_COOLDOWN_MINUTES.getter().get().longValue()));
      config.set(ServerModConfig.DIAMOND_WATCH_COOLDOWN_MINUTES.key(), String.valueOf(ServerModConfig.DIAMOND_WATCH_COOLDOWN_MINUTES.getter().get().longValue()));
      config.set(ServerModConfig.NETHERITE_WATCH_COOLDOWN_MINUTES.key(), String.valueOf(ServerModConfig.NETHERITE_WATCH_COOLDOWN_MINUTES.getter().get().longValue()));
      config.set(ServerModConfig.GOLDEN_TIME_ADVANCEMENT_TICKS.key(), String.valueOf(ServerModConfig.GOLDEN_TIME_ADVANCEMENT_TICKS.getter().get().longValue()));
      config.set(ServerModConfig.DIAMOND_TIME_ADVANCEMENT_TICKS.key(), String.valueOf(ServerModConfig.DIAMOND_TIME_ADVANCEMENT_TICKS.getter().get().longValue()));
      config.set(ServerModConfig.NETHERITE_TIME_ADVANCEMENT_TICKS.key(), String.valueOf(ServerModConfig.NETHERITE_TIME_ADVANCEMENT_TICKS.getter().get().longValue()));

      config.save();
    } catch (Exception e) {
      Constants.LOG.error("failed to read or write config file", e);
    }
  }
}
