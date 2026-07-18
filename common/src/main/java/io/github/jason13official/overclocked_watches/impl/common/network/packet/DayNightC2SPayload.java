package io.github.jason13official.overclocked_watches.impl.common.network.packet;

import io.github.jason13official.overclocked_watches.OverclockedWatches;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record DayNightC2SPayload() implements CustomPacketPayload {

  public static final CustomPacketPayload.Type<DayNightC2SPayload> TYPE = new CustomPacketPayload.Type<>(OverclockedWatches.identifier("day_night"));

  public static final StreamCodec<FriendlyByteBuf, DayNightC2SPayload> STREAM_CODEC = StreamCodec.unit(new DayNightC2SPayload());

  @Override
  public CustomPacketPayload.Type<DayNightC2SPayload> type() {
    return TYPE;
  }
}
