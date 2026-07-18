package io.github.jason13official.overclocked_watches;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.jason13official.overclocked_watches.client.entity.renderer.TrinketRenderers;
import io.github.jason13official.overclocked_watches.client.network.packet.FabricConfigSyncClientHandler;
import io.github.jason13official.overclocked_watches.impl.client.ClientModConfig;
import io.github.jason13official.overclocked_watches.impl.client.DayNightKeyPressHandler;
import io.github.jason13official.overclocked_watches.impl.client.item.RendererLayers;
import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import io.github.jason13official.overclocked_watches.impl.common.network.packet.ConfigSyncPayload;
import io.github.jason13official.overclocked_watches.impl.common.network.packet.DayNightC2SPayload;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModParticles;
import io.github.jason13official.overclocked_watches.impl.common.util.TimeManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.packs.PackType;

public class OverclockedWatchesClientFabric implements ClientModInitializer {

  public static KeyMapping dayNightKey;

  @Override
  public void onInitializeClient() {
    OverclockedWatchesClient.init();

    ClientPlayNetworking.registerGlobalReceiver(ConfigSyncPayload.TYPE, FabricConfigSyncClientHandler::registerS2CPacketHandler);

    RendererLayers.register((layerLocation, supplier) -> EntityModelLayerRegistry.registerModelLayer(layerLocation, supplier::get));

    ModParticles.registerProviders((particleType, provider) -> ParticleFactoryRegistry.getInstance().register(particleType, provider::apply));

    ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new TrinketRenderers());

    dayNightKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
        Constants.KEY_DAY_NIGHT,
        InputConstants.Type.KEYSYM,
        Math.toIntExact(ClientModConfig.DEFAULT_DAY_NIGHT_KEY.get()),
        Constants.KEY_CATEGORY_DAY_NIGHT));

    ClientTickEvents.END_CLIENT_TICK.register(client -> {

      if (dayNightKey.consumeClick()) {

        ClientLevel level = client.level;
        LocalPlayer player = client.player;
        if (level == null || player == null) {
          return;
        }

        DayNightKeyPressHandler.handle(level, player);

        ClientPlayNetworking.send(new DayNightC2SPayload());
      }
    });

    ClientTickEvents.START_WORLD_TICK.register(clientLevel -> {
      if (ServerModConfig.USE_LONG_TIME_DELTA.get() && TimeManager.CLIENT.shouldOperate()) {
        TimeManager.CLIENT.operate(clientLevel);
      }
    });
  }
}
