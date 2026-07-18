package io.github.jason13official.overclocked_watches.impl.common.registry;

import io.github.jason13official.overclocked_watches.OverclockedWatches;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public class ModParticles {

    public static final SimpleParticleType GOLDEN_WATCH_GROWTH = new SimpleParticleType(false){};
    public static final SimpleParticleType DIAMOND_WATCH_GROWTH = new SimpleParticleType(false){};
    public static final SimpleParticleType NETHERITE_WATCH_GROWTH = new SimpleParticleType(false){};

    public static void register(BiConsumer<ParticleType<?>, ResourceLocation> consumer) {
        consumer.accept(GOLDEN_WATCH_GROWTH, OverclockedWatches.identifier("golden_watch_growth"));
        consumer.accept(DIAMOND_WATCH_GROWTH, OverclockedWatches.identifier("diamond_watch_growth"));
        consumer.accept(NETHERITE_WATCH_GROWTH, OverclockedWatches.identifier("netherite_watch_growth"));
    }
}
