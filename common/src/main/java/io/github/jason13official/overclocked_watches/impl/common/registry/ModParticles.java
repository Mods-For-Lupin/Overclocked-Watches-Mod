package io.github.jason13official.overclocked_watches.impl.common.registry;

import io.github.jason13official.overclocked_watches.OverclockedWatches;
import io.github.jason13official.overclocked_watches.impl.common.particle.WatchGrowthParticle;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;

public class ModParticles {

  public static SimpleParticleType GOLDEN_WATCH_GROWTH; // = new SimpleLimitedParticle();
  public static SimpleParticleType DIAMOND_WATCH_GROWTH; // = new SimpleLimitedParticle();
  public static SimpleParticleType NETHERITE_WATCH_GROWTH; // = new SimpleLimitedParticle();

  public static void register(BiConsumer<ParticleType<?>, ResourceLocation> consumer) {

    GOLDEN_WATCH_GROWTH = new SimpleLimitedParticle();
    DIAMOND_WATCH_GROWTH = new SimpleLimitedParticle();
    NETHERITE_WATCH_GROWTH = new SimpleLimitedParticle();

    consumer.accept(GOLDEN_WATCH_GROWTH, OverclockedWatches.identifier("golden_watch_growth"));
    consumer.accept(DIAMOND_WATCH_GROWTH, OverclockedWatches.identifier("diamond_watch_growth"));
    consumer.accept(NETHERITE_WATCH_GROWTH, OverclockedWatches.identifier("netherite_watch_growth"));
  }

  public static void registerProviders(BiConsumer<SimpleParticleType, Function<SpriteSet, ParticleProvider<SimpleParticleType>>> consumer) {
    consumer.accept(GOLDEN_WATCH_GROWTH, WatchGrowthParticle.HappyVillagerParticleCopiedProvider::new);
    consumer.accept(DIAMOND_WATCH_GROWTH, WatchGrowthParticle.HappyVillagerParticleCopiedProvider::new);
    consumer.accept(NETHERITE_WATCH_GROWTH, WatchGrowthParticle.HappyVillagerParticleCopiedProvider::new);
  }

  public static class SimpleLimitedParticle extends SimpleParticleType {

    public SimpleLimitedParticle() {
      super(false);
    }
  }
}
