package io.github.jason13official.overclocked_watches;

import io.github.jason13official.overclocked_watches.client.ArmRenderHandler;
import io.github.jason13official.overclocked_watches.client.KeyInputHandlerForge;
import io.github.jason13official.overclocked_watches.impl.client.item.RendererLayers;
import io.github.jason13official.overclocked_watches.impl.client.item.RendererUtil;
import io.github.jason13official.overclocked_watches.impl.client.item.model.ArmsModel;
import io.github.jason13official.overclocked_watches.impl.client.item.renderer.WatchRenderer;
import io.github.jason13official.overclocked_watches.impl.common.ServerModConfig;
import io.github.jason13official.overclocked_watches.core.network.ForgeNetwork;
import io.github.jason13official.overclocked_watches.core.network.packet.ForgeDayNightC2SPacket;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModItems;
import io.github.jason13official.overclocked_watches.impl.common.registry.ModParticles;
import io.github.jason13official.overclocked_watches.impl.common.util.TimeManager;
import io.github.jason13official.overclocked_watches.impl.common.world.particle.WatchGrowthParticle;
import io.github.jason13official.overclocked_watches.mixin.LivingEntityRendererAccessor;
import io.github.jason13official.overclocked_watches.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.client.render.CuriosLayer;

import java.util.Set;
import java.util.function.Consumer;

public class OverclockedWatchesClientForge {

    @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModBusEvents {

        @SubscribeEvent
        public static void onClientSetup(final FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                OverclockedWatchesClient.init();
            });
        }
    }

    // @Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    // public static class ForgeModBusEvents {}

    public OverclockedWatchesClientForge(IEventBus modEventBus) {

        modEventBus.addListener(this::onRegisterEntityLayerDefinitions);
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::onAddEntityRendererLayers);
        modEventBus.addListener(this::onRegisterParticleProviders);
        modEventBus.addListener(this::onRegisterKeyMappings);

        ArmRenderHandler.setup();

        MinecraftForge.EVENT_BUS.addListener(this::onKeyInput);
        MinecraftForge.EVENT_BUS.addListener((Consumer<TickEvent.ClientTickEvent>) consumer -> {
            if (consumer.phase == TickEvent.Phase.END) return;
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null || level.dimension() != Level.OVERWORLD) return;
            if (level.getGameTime() % 2 != 0) return;
            if (ServerModConfig.useLongTimeDelta && TimeManager.CLIENT.shouldOperate()) TimeManager.CLIENT.operate(level);
            System.out.println("client " + level.getDayTime());
        });
    }

    public void onRegisterEntityLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {

        event.registerLayerDefinition(RendererLayers.GOLDEN_WATCH_WIDE, RendererLayers.layer(() -> RendererUtil.createWatchModel(false), 32, 32));
        event.registerLayerDefinition(RendererLayers.GOLDEN_WATCH_SLIM, RendererLayers.layer(() -> RendererUtil.createWatchModel(true), 32, 32));
        event.registerLayerDefinition(RendererLayers.DIAMOND_WATCH_WIDE, RendererLayers.layer(() -> RendererUtil.createWatchModel(false), 32, 32));
        event.registerLayerDefinition(RendererLayers.DIAMOND_WATCH_SLIM, RendererLayers.layer(() -> RendererUtil.createWatchModel(true), 32, 32));
        event.registerLayerDefinition(RendererLayers.NETHERITE_WATCH_WIDE, RendererLayers.layer(() -> RendererUtil.createWatchModel(false), 32, 32));
        event.registerLayerDefinition(RendererLayers.NETHERITE_WATCH_SLIM, RendererLayers.layer(() -> RendererUtil.createWatchModel(true), 32, 32));
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Services.PLATFORM.registerWatchRenderer(ModItems.GOLDEN_WATCH, () -> new WatchRenderer("golden_watch", ArmsModel::bakeGoldenWatchTextureOnModel));
            Services.PLATFORM.registerWatchRenderer(ModItems.DIAMOND_WATCH, () -> new WatchRenderer("diamond_watch", ArmsModel::bakeDiamondWatchTextureOnModel));
            Services.PLATFORM.registerWatchRenderer(ModItems.NETHERITE_WATCH, () -> new WatchRenderer("netherite_watch", ArmsModel::bakeNetheriteWatchTextureOnModel));
        });
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
        event.registerSpriteSet(ModParticles.GOLDEN_WATCH_GROWTH, WatchGrowthParticle.HappyVillagerParticleCopiedProvider::new);
        event.registerSpriteSet(ModParticles.DIAMOND_WATCH_GROWTH, WatchGrowthParticle.HappyVillagerParticleCopiedProvider::new);
        event.registerSpriteSet(ModParticles.NETHERITE_WATCH_GROWTH, WatchGrowthParticle.HappyVillagerParticleCopiedProvider::new);
    }

    public void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(KeyInputHandlerForge.dayNightKey);
    }

    private void onKeyInput(InputEvent.Key event) {

        if(KeyInputHandlerForge.dayNightKey.consumeClick()) {
            Minecraft client = Minecraft.getInstance();
            ClientLevel level = client.level;
            LocalPlayer player = client.player;
            if (level == null || player == null) return;

            long pAmount = ServerModConfig.goldenTimeAdvancementTicks;

            Item item = client.player.getMainHandItem().getItem();
            if (item == ModItems.DIAMOND_WATCH) pAmount = ServerModConfig.diamondTimeAdvancementTicks;
            if (item == ModItems.NETHERITE_WATCH) pAmount = ServerModConfig.netheriteTimeAdvancementTicks;

            if (!ServerModConfig.useLongTimeDelta) {
                level.setDayTime(level.getDayTime() + pAmount);
            }
            else {
                TimeManager.CLIENT.addToRemainingTime((int) pAmount);
            }

            // player.serverLevel().playLocalSound(player.position().x, player.position().y, player.position().z, SoundEvents.BELL_RESONATE, SoundSource.AMBIENT, 0.5f, 0.5f, false);
            // player.serverLevel().addParticle(ParticleTypes.END_ROD, player.position().x, player.position().y, player.position().z, 0, 0.005, 0);
            player.playSound(SoundEvents.BELL_BLOCK, 1, 1);
            player.playSound(SoundEvents.BELL_RESONATE, 1, 1);

            ForgeNetwork.sendToServer(new ForgeDayNightC2SPacket());
        }
    }
}
