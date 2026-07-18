package io.github.jason13official.overclocked_watches;

import io.github.jason13official.overclocked_watches.core.curio.WearableWatchCurio;
import io.github.jason13official.overclocked_watches.core.network.ForgeNetwork;
import io.github.jason13official.overclocked_watches.core.network.packet.ForgeConfigSyncS2CPacket;
import io.github.jason13official.overclocked_watches.impl.common.ModConfigIO;
import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import io.github.jason13official.overclocked_watches.impl.common.item.WatchItem;
import io.github.jason13official.overclocked_watches.impl.common.network.packet.ConfigSyncPayload;
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
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
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

    NeoForge.EVENT_BUS.addGenericListener(ItemStack.class, this::onAttachCapabilities);

    ForgeNetwork.register();

    NeoForge.EVENT_BUS.addListener((Consumer<EntityJoinLevelEvent>) event -> {
      if (!(event.getEntity() instanceof ServerPlayer player)) {
        return;
      }
      ForgeNetwork.sendToPlayer(new ForgeConfigSyncS2CPacket(ConfigSyncPayload.fromServerConfig()), player);
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

  private void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
    if (event.getObject().getItem() instanceof WatchItem item) {
      event.addCapability(CuriosCapability.ID_ITEM, CurioItemCapability.createProvider(new WearableWatchCurio(item, event.getObject())));
    }
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