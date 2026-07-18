package io.github.jason13official.overclocked_watches;

import io.github.jason13official.overclocked_watches.core.curio.WearableWatchCurio;
import io.github.jason13official.overclocked_watches.core.network.ForgeNetwork;
import io.github.jason13official.overclocked_watches.impl.common.ModConfigIO;
import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import io.github.jason13official.overclocked_watches.impl.common.item.WatchItem;
import io.github.jason13official.overclocked_watches.impl.common.network.packet.ConfigSyncPayload;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModDataComponents;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModParticles;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModTabs;
import io.github.jason13official.overclocked_watches.impl.common.util.OverclockedWatchesUtil;
import io.github.jason13official.overclocked_watches.impl.common.util.TimeManager;
import io.github.jason13official.overclocked_watches.platform.Services;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import top.theillusivec4.curios.api.CuriosCapability;

@Mod(Constants.MOD_ID)
public class OverclockedWatchesNeoForge {

  public static IEventBus EVENT_BUS;

  public OverclockedWatchesNeoForge(IEventBus modEventBus) {

    EVENT_BUS = modEventBus;

    OverclockedWatches.init();

    bind(Registries.ITEM, ModItems::register);
    bind(Registries.CREATIVE_MODE_TAB, ModTabs::register);
    bind(Registries.PARTICLE_TYPE, ModParticles::register);
    bind(Registries.DATA_COMPONENT_TYPE, ModDataComponents::register);

    EVENT_BUS.addListener(this::registerCapabilities);

    EVENT_BUS.addListener(ForgeNetwork::register);

    NeoForge.EVENT_BUS.addListener((Consumer<EntityJoinLevelEvent>) event -> {
      if (!(event.getEntity() instanceof ServerPlayer player)) {
        return;
      }
      ForgeNetwork.sendToPlayer(ConfigSyncPayload.fromServerConfig(), player);
    });

    NeoForge.EVENT_BUS.addListener((Consumer<ServerTickEvent.Pre>) consumer -> {
      ServerLevel level = consumer.getServer().overworld();
      if (ServerModConfig.USE_LONG_TIME_DELTA.get() && TimeManager.SERVER.shouldOperate()) {
        TimeManager.SERVER.operate(level);
      }
    });

    NeoForge.EVENT_BUS.addListener((Consumer<PlayerEvent.PlayerLoggedInEvent>) consumer -> {
      Player player = consumer.getEntity();
      OverclockedWatchesUtil.loadCooldowns(player.getPersistentData(), player);
      if (Services.PLATFORM.isDevelopmentEnvironment()) {
        Constants.LOG.info("[OverclockedWatches] loadCooldowns on login for {}", player.getGameProfile().getName());
      }
    });

    NeoForge.EVENT_BUS.addListener((Consumer<PlayerEvent.SaveToFile>) consumer -> {
      Player player = consumer.getEntity();
      OverclockedWatchesUtil.saveCooldowns(player.getPersistentData(), player);
      if (Services.PLATFORM.isDevelopmentEnvironment()) {
        Constants.LOG.info("[OverclockedWatches] saveCooldowns on SaveToFile for {}", player.getGameProfile().getName());
      }
    });

    NeoForge.EVENT_BUS.addListener((Consumer<PlayerEvent.Clone>) consumer ->
        OverclockedWatchesUtil.copyCooldowns(consumer.getOriginal(), consumer.getEntity()));

    NeoForge.EVENT_BUS.addListener((Consumer<AddReloadListenerEvent>) event -> {
      event.addListener(new ResourceReloadListener());
    });

    if (FMLLoader.getDist() == Dist.CLIENT) {
      new OverclockedWatchesClientNeoForge(EVENT_BUS);
    }
  }

  private void registerCapabilities(RegisterCapabilitiesEvent event) {
    event.registerItem(CuriosCapability.ITEM,
        (itemStack, context) -> new WearableWatchCurio((WatchItem) itemStack.getItem(), itemStack),
        ModItems.GOLDEN_WATCH, ModItems.DIAMOND_WATCH, ModItems.NETHERITE_WATCH);
  }

  public <T> void bind(ResourceKey<Registry<T>> registryKey, Consumer<BiConsumer<T, ResourceLocation>> source) {

    EVENT_BUS.addListener((Consumer<RegisterEvent>) event -> {
      if (registryKey.equals(event.getRegistryKey())) {
        source.accept((t, rl) -> event.register(registryKey, rl, () -> t));
      }
    });
  }

  public static class ResourceReloadListener extends SimplePreparableReloadListener<Void> {

    @Override
    public String getName() {

      return OverclockedWatches.identifier(Constants.MOD_ID).toString();
    }

    @Override
    protected void apply(Void unused, ResourceManager resourceManager, ProfilerFiller profilerFiller) {

      ModConfigIO.load(Services.PLATFORM.getConfigDirectory());
    }

    @Override
    protected Void prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {

      return null;
    }
  }
}