package com.cursee.overclocked_watches;

import com.cursee.monolib.core.sailing.Sailing;
import com.cursee.overclocked_watches.client.KeyInputHandlerForge;
import com.cursee.overclocked_watches.core.CommonConfigValues;
import com.cursee.overclocked_watches.core.ForgeCommonConfigHandler;
import com.cursee.overclocked_watches.core.curio.WearableWatchCurio;
import com.cursee.overclocked_watches.core.network.packet.ForgeConfigSyncS2CPacket;
import com.cursee.overclocked_watches.core.util.OverclockedWatchesUtil;
import com.cursee.overclocked_watches.core.util.TimeManager;
import com.cursee.overclocked_watches.core.world.item.WatchItem;
import com.cursee.overclocked_watches.core.network.ForgeNetwork;
import com.cursee.overclocked_watches.core.network.packet.ForgeDayNightC2SPacket;
import com.cursee.overclocked_watches.core.registry.RegistryForge;
import com.cursee.overclocked_watches.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.common.capability.CurioItemCapability;

import java.util.function.Consumer;

@Mod(Constants.MOD_ID)
public class OverclockedWatchesForge {

    public static IEventBus EVENT_BUS = null;
    
    public OverclockedWatchesForge(FMLJavaModLoadingContext context) {
        OverclockedWatches.init();
        // Sailing.register(Constants.MOD_NAME, Constants.MOD_ID, Constants.MOD_VERSION, Constants.MC_VERSION_RAW, Constants.PUBLISHER_AUTHOR, Constants.PRIMARY_CURSEFORGE_MODRINTH);
        Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);

        ForgeCommonConfigHandler.onLoad();

        EVENT_BUS = context.getModEventBus();

        RegistryForge.register(EVENT_BUS);
        if (FMLEnvironment.dist == Dist.CLIENT) new OverclockedWatchesClientForge(EVENT_BUS);

        MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, this::onAttachCapabilities);
        MinecraftForge.EVENT_BUS.addListener(this::onKeyInput);

        ForgeNetwork.register();

        MinecraftForge.EVENT_BUS.addListener((Consumer<EntityJoinLevelEvent>) event -> {
            if (!(event.getEntity() instanceof ServerPlayer player)) return;
            ForgeNetwork.sendToPlayer(new ForgeConfigSyncS2CPacket(
                    CommonConfigValues.day_night_cycle_allowed,
                    CommonConfigValues.default_day_night_key,
                    CommonConfigValues.use_long_time_delta,
                    CommonConfigValues.long_time_delta,
                    CommonConfigValues.golden_watch_durability,
                    CommonConfigValues.diamond_watch_durability,
                    CommonConfigValues.netherite_watch_durability,
                    CommonConfigValues.golden_watch_charges,
                    CommonConfigValues.diamond_watch_charges,
                    CommonConfigValues.netherite_watch_charges,
                    CommonConfigValues.golden_watch_cooldown_minutes,
                    CommonConfigValues.diamond_watch_cooldown_minutes,
                    CommonConfigValues.netherite_watch_cooldown_minutes,
                    CommonConfigValues.golden_time_advancement_ticks,
                    CommonConfigValues.diamond_time_advancement_ticks,
                    CommonConfigValues.netherite_time_advancement_ticks
            ), player);
        });

        MinecraftForge.EVENT_BUS.addListener((Consumer<TickEvent.ServerTickEvent>) consumer -> {
            if (consumer.phase == TickEvent.Phase.END) return;
            MinecraftServer server = consumer.getServer();
            // if (server.getTickCount() % 2 != 0) return;
            if (CommonConfigValues.use_long_time_delta && TimeManager.shouldOperate()) TimeManager.operate(server.overworld());
        });

        MinecraftForge.EVENT_BUS.addListener((Consumer<TickEvent.ClientTickEvent>) consumer -> {
            if (consumer.phase == TickEvent.Phase.END || Minecraft.getInstance().level == null) return;
            // if (server.getTickCount() % 2 != 0) return;
            if (CommonConfigValues.use_long_time_delta && TimeManager.shouldOperate()) TimeManager.operate(Minecraft.getInstance().level);
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
    }

    private void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof WatchItem item) {
            event.addCapability(CuriosCapability.ID_ITEM, CurioItemCapability.createProvider(new WearableWatchCurio(item, event.getObject())));
        }
    }

    private void onKeyInput(InputEvent.Key event) {
        if(KeyInputHandlerForge.dayNightKey.consumeClick()) {
            ForgeNetwork.sendToServer(new ForgeDayNightC2SPacket());
        }
    }
}