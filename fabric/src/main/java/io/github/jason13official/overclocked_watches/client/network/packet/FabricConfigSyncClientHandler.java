package io.github.jason13official.overclocked_watches.client.network.packet;

import io.github.jason13official.overclocked_watches.impl.common.network.packet.ConfigSyncPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class FabricConfigSyncClientHandler {

  public static void registerS2CPacketHandler(ConfigSyncPayload payload, ClientPlayNetworking.Context context) {
    payload.applyToConfig();
  }
}
