package io.github.jason13official.overclocked_watches.impl.common.registry;

import io.github.jason13official.overclocked_watches.OverclockedWatches;
import java.util.function.BiConsumer;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;

public class ModParticles {

  public static final SimpleParticleType GOLDEN_WATCH_GROWTH = new SimpleLimitedParticle();
  public static final SimpleParticleType DIAMOND_WATCH_GROWTH = new SimpleLimitedParticle();
  public static final SimpleParticleType NETHERITE_WATCH_GROWTH = new SimpleLimitedParticle();

  public static void register(BiConsumer<ParticleType<?>, ResourceLocation> consumer) {
    consumer.accept(GOLDEN_WATCH_GROWTH, OverclockedWatches.identifier("golden_watch_growth"));
    consumer.accept(DIAMOND_WATCH_GROWTH, OverclockedWatches.identifier("diamond_watch_growth"));
    consumer.accept(NETHERITE_WATCH_GROWTH, OverclockedWatches.identifier("netherite_watch_growth"));
  }

  public static class SimpleLimitedParticle extends SimpleParticleType {

    public SimpleLimitedParticle() {
      super(false);
    }
  }
}
