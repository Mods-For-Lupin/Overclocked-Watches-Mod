package io.github.jason13official.overclocked_watches;

import io.github.jason13official.overclocked_watches.core.network.FabricNetwork;
import io.github.jason13official.overclocked_watches.core.network.packet.FabricConfigSyncS2CPacket;
import io.github.jason13official.overclocked_watches.core.registry.RegistryFabric;
import io.github.jason13official.overclocked_watches.impl.common.util.IEntityDataSaver;
import io.github.jason13official.overclocked_watches.impl.common.util.OverclockedWatchesUtil;
import io.github.jason13official.overclocked_watches.impl.common.util.TimeManager;
import io.github.jason13official.overclocked_watches.impl.common.ModConfigIO;
import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import io.github.jason13official.overclocked_watches.platform.Services;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public class OverclockedWatchesFabric implements ModInitializer {

  @Override
  public void onInitialize() {
    OverclockedWatches.init();

    // handled in init
    // FabricCommonConfigHandler.onLoad();

    RegistryFabric.register();
    FabricNetwork.Packets.registerPacketIDsAndReceivers();

    ServerEntityEvents.ENTITY_LOAD.register(FabricConfigSyncS2CPacket::registerS2CPacketSender);

//        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
//            OverclockedWatchesUtil.loadCooldowns(((IEntityDataSaver) handler.player).getPersistentData(), handler.player);
//        });

    ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
      if (!(entity instanceof ServerPlayer player)) {
        return;
      }
      OverclockedWatchesUtil.loadCooldowns(((IEntityDataSaver) player).getPersistentData(), player);
    });

//        ServerPlayerEvents.AFTER_RESPAWN.register((entity, world, isAlive) -> {
//            // if (!(entity instanceof Player player)) return;
//            // OverclockedWatchesUtil.loadCooldowns(((IEntityDataSaver) player).getPersistentData(), player);
//        });

    ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, isAlive) -> {
      // if (!isAlive) return;
      CompoundTag temp = new CompoundTag();
      OverclockedWatchesUtil.saveCooldowns(temp, oldPlayer);
      OverclockedWatchesUtil.loadCooldowns(temp, newPlayer);
    });

    ServerTickEvents.START_SERVER_TICK.register(server -> {
      // if (server.getTickCount() % 2 != 0) return;
      if (TimeManager.SERVER == null) {
        TimeManager.SERVER = new TimeManager();
      }
      if (ServerModConfig.useLongTimeDelta && TimeManager.SERVER.shouldOperate()) {
        TimeManager.SERVER.operate(server.overworld());
      }
    });

    ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new ResourceReloadListener());
  }

  public static class ResourceReloadListener implements SimpleSynchronousResourceReloadListener {

    @Override
    public ResourceLocation getFabricId() {

      return OverclockedWatches.identifier(Constants.MOD_ID);
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {

      ModConfigIO.load(Services.PLATFORM.getConfigDirectory());
    }
  }
}
