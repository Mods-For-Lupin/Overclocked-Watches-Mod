package io.github.jason13official.overclocked_watches.core.network.packet;

import io.github.jason13official.overclocked_watches.client.network.packet.ForgeConfigSyncClientHandler;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public record ForgeConfigSyncS2CPacket(boolean day_night_cycle_allowed, long default_day_night_key, boolean use_long_time_delta, long long_time_delta, long golden_watch_durability,
                                       long diamond_watch_durability, long netherite_watch_durability, long golden_watch_charges, long diamond_watch_charges, long netherite_watch_charges,
                                       long golden_watch_cooldown_minutes, long diamond_watch_cooldown_minutes, long netherite_watch_cooldown_minutes, long golden_time_advancement_ticks,
                                       long diamond_time_advancement_ticks, long netherite_time_advancement_ticks) {

  public static ForgeConfigSyncS2CPacket decode(FriendlyByteBuf data) {
    return new ForgeConfigSyncS2CPacket(
        data.readBoolean(), data.readLong(), data.readBoolean(), data.readLong(),
        data.readLong(), data.readLong(), data.readLong(), data.readLong(),
        data.readLong(), data.readLong(), data.readLong(), data.readLong(),
        data.readLong(), data.readLong(), data.readLong(), data.readLong()
    );
  }

  public static void handle(ForgeConfigSyncS2CPacket packet, Supplier<Context> contextSupplier) {
    contextSupplier.get().enqueueWork(() ->
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ForgeConfigSyncClientHandler.registerS2CPacketHandler(packet, contextSupplier))
    );
    contextSupplier.get().setPacketHandled(true);
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
}
