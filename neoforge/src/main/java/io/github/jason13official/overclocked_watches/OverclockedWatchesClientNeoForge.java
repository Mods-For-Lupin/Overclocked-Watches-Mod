package io.github.jason13official.overclocked_watches;

import io.github.jason13official.overclocked_watches.client.ArmRenderHandler;
import io.github.jason13official.overclocked_watches.client.KeyInputHandlerForge;
import io.github.jason13official.overclocked_watches.core.network.ForgeNetwork;
import io.github.jason13official.overclocked_watches.core.network.packet.ForgeDayNightC2SPacket;
import io.github.jason13official.overclocked_watches.impl.client.DayNightKeyPressHandler;
import io.github.jason13official.overclocked_watches.impl.client.item.RendererLayers;
import io.github.jason13official.overclocked_watches.impl.client.item.renderer.WatchRenderer;
import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModParticles;
import io.github.jason13official.overclocked_watches.impl.common.util.TimeManager;
import io.github.jason13official.overclocked_watches.mixin.LivingEntityRendererAccessor;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.NeoForge;
import top.theillusivec4.curios.client.render.CuriosLayer;

public class OverclockedWatchesClientNeoForge {

  public OverclockedWatchesClientNeoForge(IEventBus modEventBus) {

    OverclockedWatchesClient.init();

    modEventBus.addListener(this::onRegisterEntityLayerDefinitions);
    modEventBus.addListener(this::onClientSetup);
    modEventBus.addListener(this::onAddEntityRendererLayers);
    modEventBus.addListener(this::onRegisterParticleProviders);
    modEventBus.addListener(this::onRegisterKeyMappings);

    ArmRenderHandler.setup();

    NeoForge.EVENT_BUS.addListener(this::onKeyInput);
    NeoForge.EVENT_BUS.addListener((Consumer<ClientTickEvent.Pre>) event -> {
      ClientLevel level = Minecraft.getInstance().level;
      if (level == null || level.dimension() != Level.OVERWORLD) {
        return;
      }
      if (ServerModConfig.USE_LONG_TIME_DELTA.get() && TimeManager.CLIENT.shouldOperate()) {
        TimeManager.CLIENT.operate(level);
      }
    });
  }

  public void onRegisterEntityLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {

    RendererLayers.register(event::registerLayerDefinition);
  }

  public void onClientSetup(FMLClientSetupEvent event) {
    event.enqueueWork(WatchRenderer::registerAll);
  }

  public void onAddEntityRendererLayers(EntityRenderersEvent.AddLayers event) {
    Set<EntityType<?>> entities = Set.of(EntityType.ZOMBIE,
        EntityType.HUSK,
        EntityType.DROWNED,
        EntityType.SKELETON,
        EntityType.STRAY,
        EntityType.WITHER_SKELETON,
        EntityType.PIGLIN,
        EntityType.PIGLIN_BRUTE,
        EntityType.ZOMBIFIED_PIGLIN);
    loop:
    for (EntityType<?> entity : entities) {
      EntityRenderer<?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().renderers.get(entity);
      if (renderer == null) {
        continue;
      }
      LivingEntityRenderer livingEntityRenderer = (LivingEntityRenderer<?, ?>) renderer;
      for (RenderLayer<?, ?> layer : ((LivingEntityRendererAccessor<?, ?>) livingEntityRenderer).getLayers()) {
        if (layer instanceof CuriosLayer<?, ?>) {
          continue loop;
        }
      }
      livingEntityRenderer.addLayer(new CuriosLayer<>(livingEntityRenderer));
    }
  }

  public void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
    ModParticles.registerProviders((particleType, provider) -> event.registerSpriteSet(particleType, provider::apply));
  }

  public void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
    event.register(KeyInputHandlerForge.dayNightKey);
  }

  private void onKeyInput(InputEvent.Key event) {

    if (KeyInputHandlerForge.dayNightKey.consumeClick()) {
      Minecraft client = Minecraft.getInstance();
      ClientLevel level = client.level;
      LocalPlayer player = client.player;
      if (level == null || player == null) {
        return;
      }

      DayNightKeyPressHandler.handle(level, player);

      ForgeNetwork.sendToServer(new ForgeDayNightC2SPacket());
    }
  }

//  @EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
//  public static class ModClientBusEvents {
//
//    @SubscribeEvent
//    public static void onClientSetup(final FMLClientSetupEvent event) {
//      event.enqueueWork(() -> {
//        OverclockedWatchesClient.init();
//      });
//    }
//  }
}
