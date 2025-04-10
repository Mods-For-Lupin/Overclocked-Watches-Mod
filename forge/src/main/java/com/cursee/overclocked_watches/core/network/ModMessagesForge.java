package com.cursee.overclocked_watches.core.network;

import com.cursee.overclocked_watches.Constants;
import com.cursee.overclocked_watches.OverclockedWatches;
import com.cursee.overclocked_watches.core.network.packet.ForgeDayNightC2SPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessagesForge {

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
    }
    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }
}
