package io.github.jason13official.overclocked_watches.core.network.packet;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class ForgeDayNightC2SPacket {

  public ForgeDayNightC2SPacket() {
  }

  public ForgeDayNightC2SPacket(FriendlyByteBuf buf) {
  }

  public void toBytes(FriendlyByteBuf buf) {
  }

  public boolean handle(Supplier<NetworkEvent.Context> supplier) {
    NetworkEvent.Context context = supplier.get();
    context.enqueueWork(() -> {
      ServerPlayer player = context.getSender();
      if (player == null || player.getServer() == null) {
        return;
      }
      DayNightC2SHandler.handle(player.getServer(), player);
    });

    return true;
  }
}
