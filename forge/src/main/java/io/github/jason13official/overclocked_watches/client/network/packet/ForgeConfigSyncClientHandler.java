package io.github.jason13official.overclocked_watches.client.network.packet;

import io.github.jason13official.overclocked_watches.core.network.packet.ForgeConfigSyncS2CPacket;
import java.util.function.Supplier;
import net.minecraftforge.network.NetworkEvent;

public class ForgeConfigSyncClientHandler {

  public static void registerS2CPacketHandler(ForgeConfigSyncS2CPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
    contextSupplier.get().enqueueWork(() -> packet.payload().applyToConfig());
  }
}
