package com.cursee.overclocked_watches.core;

public class CommonConfigValues {

    public static boolean day_night_cycle_allowed = true;
    public static long default_day_night_key = 79;

    public static boolean use_long_time_delta = true;
    public static long long_time_delta = 100L; // 500 ? (also ever 5 ticks ????)

    public static long golden_watch_durability = 100L;
    public static long diamond_watch_durability = 300L;
    public static long netherite_watch_durability = 500L;

    public static long golden_watch_charges = 1L;
    public static long diamond_watch_charges = 3L;
    public static long netherite_watch_charges = 5L;

    public static long golden_watch_cooldown_minutes = 20L;
    public static long diamond_watch_cooldown_minutes = 10L;
    public static long netherite_watch_cooldown_minutes = 5L;

    public static long golden_time_advancement_ticks = 12_000L;
    public static long diamond_time_advancement_ticks = 12_000L;
    public static long netherite_time_advancement_ticks = 12_000L;
}
