package io.github.jason13official.overclocked_watches.core.network.packet;

import io.github.jason13official.overclocked_watches.client.network.packet.ForgeConfigSyncClientHandler;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public record ForgeConfigSyncS2CPacket(ConfigSyncPayload payload) {

  public static ForgeConfigSyncS2CPacket decode(FriendlyByteBuf data) {
    return new ForgeConfigSyncS2CPacket(ConfigSyncPayload.read(data));
  }

  public static void handle(ForgeConfigSyncS2CPacket packet, Supplier<Context> contextSupplier) {
    contextSupplier.get().enqueueWork(() ->
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ForgeConfigSyncClientHandler.registerS2CPacketHandler(packet, contextSupplier))
    );
    contextSupplier.get().setPacketHandled(true);
  }

  public void encode(FriendlyByteBuf data) {
    payload.write(data);
  }
}
