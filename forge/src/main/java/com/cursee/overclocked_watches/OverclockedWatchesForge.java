package com.cursee.overclocked_watches;

import com.cursee.overclocked_watches.core.registry.RegistryForge;
import com.cursee.monolib.core.sailing.Sailing;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class OverclockedWatchesForge {

    public static IEventBus EVENT_BUS = null;
    
    public OverclockedWatchesForge(FMLJavaModLoadingContext context) {
        OverclockedWatches.init();
        Sailing.register(Constants.MOD_ID, Constants.MOD_NAME, Constants.MOD_VERSION, Constants.MOD_PUBLISHER, Constants.MOD_URL);
        OverclockedWatchesForge.EVENT_BUS = context.getModEventBus();
        RegistryForge.register(OverclockedWatchesForge.EVENT_BUS);
    }
}