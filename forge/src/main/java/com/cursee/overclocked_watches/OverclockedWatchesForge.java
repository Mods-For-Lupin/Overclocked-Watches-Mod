package com.cursee.overclocked_watches;

import com.cursee.monolib.core.sailing.Sailing;
import com.cursee.overclocked_watches.client.KeyInputHandlerForge;
import com.cursee.overclocked_watches.core.curio.WearableWatchCurio;
import com.cursee.overclocked_watches.core.world.item.WatchItem;
import com.cursee.overclocked_watches.core.network.ModMessagesForge;
import com.cursee.overclocked_watches.core.network.packet.ForgeDayNightC2SPacket;
import com.cursee.overclocked_watches.core.registry.RegistryForge;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.common.capability.CurioItemCapability;

@Mod(Constants.MOD_ID)
public class OverclockedWatchesForge {

    public static IEventBus EVENT_BUS = null;
    
    public OverclockedWatchesForge(FMLJavaModLoadingContext context) {
        OverclockedWatches.init();
        // Sailing.register(Constants.MOD_NAME, Constants.MOD_ID, Constants.MOD_VERSION, Constants.MC_VERSION_RAW, Constants.PUBLISHER_AUTHOR, Constants.PRIMARY_CURSEFORGE_MODRINTH);
        Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);

        EVENT_BUS = context.getModEventBus();

        RegistryForge.register(EVENT_BUS);
        if (FMLEnvironment.dist == Dist.CLIENT) new OverclockedWatchesClientForge(EVENT_BUS);

        MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, this::onAttachCapabilities);
        MinecraftForge.EVENT_BUS.addListener(this::onKeyInput);

        ModMessagesForge.register();
    }

    private void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        if (event.getObject().getItem() instanceof WatchItem item) {
            event.addCapability(CuriosCapability.ID_ITEM, CurioItemCapability.createProvider(new WearableWatchCurio(item, event.getObject())));
        }
    }

    private void onKeyInput(InputEvent.Key event) {
        if(KeyInputHandlerForge.dayNightKey.consumeClick()) {
            ModMessagesForge.sendToServer(new ForgeDayNightC2SPacket());
        }
    }
}