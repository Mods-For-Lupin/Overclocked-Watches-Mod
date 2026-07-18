package io.github.jason13official.overclocked_watches.core.network;

import io.github.jason13official.overclocked_watches.OverclockedWatches;
import io.github.jason13official.overclocked_watches.core.network.packet.ForgeConfigSyncS2CPacket;
import io.github.jason13official.overclocked_watches.core.network.packet.ForgeDayNightC2SPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ForgeNetwork {

  private static SimpleChannel INSTANCE;

  private static int packetId = 0;

  private static int id() {
    return packetId++;
  }

  public static void register() {

    SimpleChannel net = NetworkRegistry.ChannelBuilder
        .named(OverclockedWatches.identifier("messages"))
        .networkProtocolVersion(() -> "1.0")
        .clientAcceptedVersions(s -> true)
        .serverAcceptedVersions(s -> true)
        .simpleChannel();

    INSTANCE = net;

    net.messageBuilder(ForgeDayNightC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
        .decoder(ForgeDayNightC2SPacket::new)
        .encoder(ForgeDayNightC2SPacket::toBytes)
        .consumerMainThread(ForgeDayNightC2SPacket::handle)
        .add();

    net.messageBuilder(ForgeConfigSyncS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
        .decoder(ForgeConfigSyncS2CPacket::decode)
        .encoder(ForgeConfigSyncS2CPacket::encode)
        .consumerMainThread(ForgeConfigSyncS2CPacket::handle)
        .add();
  }

  public static <MSG> void sendToServer(MSG message) {
    INSTANCE.sendToServer(message);
  }

  public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
    INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
  }
}
