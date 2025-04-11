package com.cursee.overclocked_watches.core;

import com.cursee.monolib.util.toml.Toml;
import com.cursee.overclocked_watches.Constants;
import com.cursee.overclocked_watches.platform.Services;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

public class ForgeCommonConfigHandler {

    public static final String CONFIG_DIR = Services.PLATFORM.getGameDirectory() + File.separator + "config" + File.separator;
    public static final String CONFIG_FILEPATH = CONFIG_DIR + Constants.MOD_ID + "-common.toml";

    public static void onLoad() {
        File configDirectory = new File(CONFIG_DIR);
        if (!configDirectory.isDirectory()) configDirectory.mkdirs();
        File configFile = new File(CONFIG_FILEPATH);

        if (!configFile.isFile()) createConfigurationWithDefaultValues(configFile);
        else readConfigurationAndGetValues(configFile);
    }

    private static void createConfigurationWithDefaultValues(File configFile) {

        populateDefaults();

        try (PrintWriter writer = new PrintWriter(configFile)) {

            defaults.forEach((s, o) -> {
                writer.println(s + " = " + o);
            });
        }
        catch (IOException ignored) {}
    }

    private static void readConfigurationAndGetValues(File configFile) {
        Toml toml = new Toml().read(configFile);
        CommonConfigValues.day_night_cycle_allowed = toml.getBoolean("day_night_cycle_allowed");
        CommonConfigValues.default_day_night_key = toml.getLong("default_day_night_key");
        CommonConfigValues.use_long_time_delta = toml.getBoolean("use_long_time_delta");
        CommonConfigValues.long_time_delta = toml.getLong("long_time_delta");
        CommonConfigValues.golden_watch_durability = toml.getLong("golden_watch_durability");
        CommonConfigValues.diamond_watch_durability = toml.getLong("diamond_watch_durability");
        CommonConfigValues.netherite_watch_durability = toml.getLong("netherite_watch_durability");
        CommonConfigValues.golden_watch_charges = toml.getLong("golden_watch_charges");
        CommonConfigValues.diamond_watch_charges = toml.getLong("diamond_watch_charges");
        CommonConfigValues.netherite_watch_charges = toml.getLong("netherite_watch_charges");
        CommonConfigValues.golden_watch_cooldown_minutes = toml.getLong("golden_watch_cooldown_minutes");
        CommonConfigValues.diamond_watch_cooldown_minutes = toml.getLong("diamond_watch_cooldown_minutes");
        CommonConfigValues.netherite_watch_cooldown_minutes = toml.getLong("netherite_watch_cooldown_minutes");
        CommonConfigValues.golden_time_advancement_ticks = toml.getLong("golden_time_advancement_ticks");
        CommonConfigValues.diamond_time_advancement_ticks = toml.getLong("diamond_time_advancement_ticks");
        CommonConfigValues.netherite_time_advancement_ticks = toml.getLong("netherite_time_advancement_ticks");
    }

    public static final Map<String, Object> defaults = new LinkedHashMap<>();

    public static void populateDefaults() {
        defaults.put("# day_night_cycle_allowed", "default:" + CommonConfigValues.day_night_cycle_allowed + ", whether players can press a hot-key to cycle time");
        defaults.put("day_night_cycle_allowed", CommonConfigValues.day_night_cycle_allowed);

        defaults.put("# default_day_night_key", "default:" + CommonConfigValues.default_day_night_key + ", the key registered, defaults to O change this with caution");
        defaults.put("default_day_night_key", CommonConfigValues.default_day_night_key);

        defaults.put("# use_long_time_delta", "default:" + CommonConfigValues.use_long_time_delta + ", if this is false, pressing the hotkey changes time instantly");
        defaults.put("use_long_time_delta", CommonConfigValues.use_long_time_delta);

        defaults.put("# long_time_delta", "default:" + CommonConfigValues.long_time_delta + ", if use_long_time_delta is true, this is how much time will be modified by in increments");
        defaults.put("long_time_delta", CommonConfigValues.long_time_delta);

        defaults.put("# golden_watch_durability", "default:" + CommonConfigValues.golden_watch_durability + ", how strong is the golden watch");
        defaults.put("golden_watch_durability", CommonConfigValues.golden_watch_durability);

        defaults.put("# diamond_watch_durability", "default:" + CommonConfigValues.diamond_watch_durability + ", how strong is the diamond watch");
        defaults.put("diamond_watch_durability", CommonConfigValues.diamond_watch_durability);

        defaults.put("# netherite_watch_durability", "default:" + CommonConfigValues.netherite_watch_durability + ", how strong is the netherite watch");
        defaults.put("netherite_watch_durability", CommonConfigValues.netherite_watch_durability);

        defaults.put("# golden_watch_charges", "default:" + CommonConfigValues.golden_watch_charges + ", how many changes the golden watch contains");
        defaults.put("golden_watch_charges", CommonConfigValues.golden_watch_charges);

        defaults.put("# diamond_watch_charges", "default:" + CommonConfigValues.diamond_watch_charges + ", how many changes the diamond watch contains");
        defaults.put("diamond_watch_charges", CommonConfigValues.diamond_watch_charges);

        defaults.put("# netherite_watch_charges", "default:" + CommonConfigValues.netherite_watch_charges + ", how many changes the netherite watch contains");
        defaults.put("netherite_watch_charges", CommonConfigValues.netherite_watch_charges);

        defaults.put("# golden_watch_cooldown_minutes", "default:" + CommonConfigValues.golden_watch_cooldown_minutes + ", how long until another watch can be used after using the golden watch");
        defaults.put("golden_watch_cooldown_minutes", CommonConfigValues.golden_watch_cooldown_minutes);

        defaults.put("# diamond_watch_cooldown_minutes", "default:" + CommonConfigValues.diamond_watch_cooldown_minutes + ", how long until another watch can be used after using the diamond watch");
        defaults.put("diamond_watch_cooldown_minutes", CommonConfigValues.diamond_watch_cooldown_minutes);

        defaults.put("# netherite_watch_cooldown_minutes", "default:" + CommonConfigValues.netherite_watch_cooldown_minutes + ", how long until another watch can be used after using the netherite watch");
        defaults.put("netherite_watch_cooldown_minutes", CommonConfigValues.netherite_watch_cooldown_minutes);

        defaults.put("# golden_time_advancement_ticks", "default:" + CommonConfigValues.golden_time_advancement_ticks + ", how much does the golden watch advance time by");
        defaults.put("golden_time_advancement_ticks", CommonConfigValues.golden_time_advancement_ticks);

        defaults.put("# diamond_time_advancement_ticks", "default:" + CommonConfigValues.diamond_time_advancement_ticks + ", how much does the diamond watch advance time by");
        defaults.put("diamond_time_advancement_ticks", CommonConfigValues.diamond_time_advancement_ticks);

        defaults.put("# netherite_time_advancement_ticks", "default:" + CommonConfigValues.netherite_time_advancement_ticks + ", how much does the netherite watch advance time by");
        defaults.put("netherite_time_advancement_ticks", CommonConfigValues.netherite_time_advancement_ticks);
    }
}
