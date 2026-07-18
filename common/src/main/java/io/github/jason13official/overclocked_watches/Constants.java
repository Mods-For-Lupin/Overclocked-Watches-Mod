package io.github.jason13official.overclocked_watches;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

  public static final String MOD_ID = "overclocked_watches";
  public static final String MOD_NAME = "Overclocked Watches";
  public static final String MOD_VERSION = "1.2.0";
  public static final String MOD_PUBLISHER = "Lupin";
  public static final String MOD_URL = "https://www.curseforge.com/minecraft/mc-mods/overclocked-watches";
  public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

  public static final String KEY_CATEGORY_DAY_NIGHT = "key.category.overclocked_watches.day_night";
  public static final String KEY_DAY_NIGHT = "key.overclocked_watches.day_night";

  // in seconds
  public static final int AGE_PROGRESSION_NETHERITE = 60 * 4;
  public static final int AGE_PROGRESSION_DIAMOND = 30;
  public static final int AGE_PROGRESSION_GOLD = 5;
}