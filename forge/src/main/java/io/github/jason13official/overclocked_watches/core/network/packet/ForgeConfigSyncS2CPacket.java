package io.github.jason13official.overclocked_watches.core.network.packet;

import io.github.jason13official.overclocked_watches.client.network.packet.ForgeConfigSyncClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ForgeConfigSyncS2CPacket {

    public final boolean day_night_cycle_allowed;
    public final long default_day_night_key;
    public final boolean use_long_time_delta;
    public final long long_time_delta;

    public final long golden_watch_durability;
    public final long diamond_watch_durability;
    public final long netherite_watch_durability;
    public final long golden_watch_charges;

    public final long diamond_watch_charges;
    public final long netherite_watch_charges;
    public final long golden_watch_cooldown_minutes;
    public final long diamond_watch_cooldown_minutes;

    public final long netherite_watch_cooldown_minutes;
    public final long golden_time_advancement_ticks;
    public final long diamond_time_advancement_ticks;
    public final long netherite_time_advancement_ticks;

    public ForgeConfigSyncS2CPacket(
            boolean day_night_cycle_allowed,
            long default_day_night_key,
            boolean use_long_time_delta,
            long long_time_delta,
            long golden_watch_durability,
            long diamond_watch_durability,
            long netherite_watch_durability,
            long golden_watch_charges,
            long diamond_watch_charges,
            long netherite_watch_charges,
            long golden_watch_cooldown_minutes,
            long diamond_watch_cooldown_minutes,
            long netherite_watch_cooldown_minutes,
            long golden_time_advancement_ticks,
            long diamond_time_advancement_ticks,
            long netherite_time_advancement_ticks
    ) {
        this.day_night_cycle_allowed = day_night_cycle_allowed;
        this.default_day_night_key = default_day_night_key;
        this.use_long_time_delta = use_long_time_delta;
        this.long_time_delta = long_time_delta;
        this.golden_watch_durability = golden_watch_durability;
        this.diamond_watch_durability = diamond_watch_durability;
        this.netherite_watch_durability = netherite_watch_durability;
        this.golden_watch_charges = golden_watch_charges;
        this.diamond_watch_charges = diamond_watch_charges;
        this.netherite_watch_charges = netherite_watch_charges;
        this.golden_watch_cooldown_minutes = golden_watch_cooldown_minutes;
        this.diamond_watch_cooldown_minutes = diamond_watch_cooldown_minutes;
        this.netherite_watch_cooldown_minutes = netherite_watch_cooldown_minutes;
        this.golden_time_advancement_ticks = golden_time_advancement_ticks;
        this.diamond_time_advancement_ticks = diamond_time_advancement_ticks;
        this.netherite_time_advancement_ticks = netherite_time_advancement_ticks;
    }

    public void encode(FriendlyByteBuf data) {
        data.writeBoolean(day_night_cycle_allowed);
        data.writeLong(default_day_night_key);
        data.writeBoolean(use_long_time_delta);
        data.writeLong(long_time_delta);
        data.writeLong(golden_watch_durability);
        data.writeLong(diamond_watch_durability);
        data.writeLong(netherite_watch_durability);
        data.writeLong(golden_watch_charges);
        data.writeLong(diamond_watch_charges);
        data.writeLong(netherite_watch_charges);
        data.writeLong(golden_watch_cooldown_minutes);
        data.writeLong(diamond_watch_cooldown_minutes);
        data.writeLong(netherite_watch_cooldown_minutes);
        data.writeLong(golden_time_advancement_ticks);
        data.writeLong(diamond_time_advancement_ticks);
        data.writeLong(netherite_time_advancement_ticks);
    }

    public static ForgeConfigSyncS2CPacket decode(FriendlyByteBuf data) {
        return new ForgeConfigSyncS2CPacket(
                data.readBoolean(), data.readLong(), data.readBoolean(), data.readLong(),
                data.readLong(), data.readLong(), data.readLong(), data.readLong(),
                data.readLong(), data.readLong(), data.readLong(), data.readLong(),
                data.readLong(), data.readLong(), data.readLong(), data.readLong()
        );
    }

    public static void handle(ForgeConfigSyncS2CPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        contextSupplier.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ForgeConfigSyncClientHandler.registerS2CPacketHandler(packet, contextSupplier))
        );
        contextSupplier.get().setPacketHandled(true);
    }
}
