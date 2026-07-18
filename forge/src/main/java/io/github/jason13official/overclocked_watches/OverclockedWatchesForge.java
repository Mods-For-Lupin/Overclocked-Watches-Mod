package io.github.jason13official.overclocked_watches;

import io.github.jason13official.overclocked_watches.core.curio.WearableWatchCurio;
import io.github.jason13official.overclocked_watches.core.network.ForgeNetwork;
import io.github.jason13official.overclocked_watches.core.network.packet.ForgeConfigSyncS2CPacket;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModBlocks;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModParticles;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModTabs;
import io.github.jason13official.overclocked_watches.impl.common.util.OverclockedWatchesUtil;
import io.github.jason13official.overclocked_watches.impl.common.util.TimeManager;
import io.github.jason13official.overclocked_watches.impl.common.world.item.WatchItem;
import io.github.jason13official.overclocked_watches.impl.common.ModConfigIO;
import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import io.github.jason13official.overclocked_watches.platform.Services;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.RegisterEvent;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.common.capability.CurioItemCapability;

@Mod(Constants.MOD_ID)
public class OverclockedWatchesForge {

  public static IEventBus EVENT_BUS = null;

  public OverclockedWatchesForge(FMLJavaModLoadingContext context) {

    EVENT_BUS = context.getModEventBus();

    OverclockedWatches.init();

    bind(Registries.BLOCK, ModBlocks::register);
    bind(Registries.ITEM, ModItems::register);
    bind(Registries.CREATIVE_MODE_TAB, ModTabs::register);
    bind(Registries.PARTICLE_TYPE, ModParticles::register);

    MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, this::onAttachCapabilities);

    ForgeNetwork.register();

    MinecraftForge.EVENT_BUS.addListener((Consumer<EntityJoinLevelEvent>) event -> {
      if (!(event.getEntity() instanceof ServerPlayer player)) {
        return;
      }
      ForgeNetwork.sendToPlayer(new ForgeConfigSyncS2CPacket(ServerModConfig.dayNightCycleAllowed, ServerModConfig.defaultDayNightKey, ServerModConfig.useLongTimeDelta, ServerModConfig.longTimeDelta,
              ServerModConfig.goldenWatchDurability, ServerModConfig.diamondWatchDurability, ServerModConfig.netheriteWatchDurability, ServerModConfig.goldenWatchCharges,
              ServerModConfig.diamondWatchCharges, ServerModConfig.netheriteWatchCharges, ServerModConfig.goldenWatchCooldownMinutes, ServerModConfig.diamondWatchCooldownMinutes,
              ServerModConfig.netheriteWatchCooldownMinutes, ServerModConfig.goldenTimeAdvancementTicks, ServerModConfig.diamondTimeAdvancementTicks, ServerModConfig.netheriteTimeAdvancementTicks),
          player);
    });

    MinecraftForge.EVENT_BUS.addListener((Consumer<TickEvent.ServerTickEvent>) consumer -> {
      if (consumer.phase == TickEvent.Phase.END) {
        return;
      }
      ServerLevel level = consumer.getServer().overworld();
      if (level.getGameTime() % 2 != 0) {
        return;
      }
      if (ServerModConfig.useLongTimeDelta && TimeManager.SERVER.shouldOperate()) {
        TimeManager.SERVER.operate(level);
      }
      System.out.println("server " + level.getDayTime());
    });

    MinecraftForge.EVENT_BUS.addListener((Consumer<PlayerEvent.PlayerLoggedInEvent>) consumer -> {
      Player player = consumer.getEntity();
      OverclockedWatchesUtil.loadCooldowns(player.getPersistentData(), player);
    });

    MinecraftForge.EVENT_BUS.addListener((Consumer<PlayerEvent.Clone>) consumer -> {
      CompoundTag temp = new CompoundTag();
      OverclockedWatchesUtil.saveCooldowns(temp, consumer.getOriginal());
      OverclockedWatchesUtil.loadCooldowns(temp, consumer.getEntity());
    });

    MinecraftForge.EVENT_BUS.addListener((Consumer<AddReloadListenerEvent>) event -> {
      event.addListener(new ResourceReloadListener());
    });

    if (FMLLoader.getDist() == Dist.CLIENT) {
      new OverclockedWatchesClientForge(EVENT_BUS);
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