package com.cursee.overclocked_watches.core.world.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class WatchGrowthParticle extends TextureSheetParticle {

    public WatchGrowthParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        float f = this.random.nextFloat() * 0.1F + 0.2F;
        this.rCol = f;
        this.gCol = f;
        this.bCol = f;
        this.setSize(0.02F, 0.02F);
        this.quadSize *= this.random.nextFloat() * 0.6F + 0.5F;
        this.xd *= 0.019999999552965164;
        this.yd *= 0.019999999552965164;
        this.zd *= 0.019999999552965164;
        this.lifetime = (int)(20.0 / (Math.random() * 0.8 + 0.2));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    /** @see SuspendedTownParticle */
    public static class HappyVillagerParticleCopiedProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public HappyVillagerParticleCopiedProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            WatchGrowthParticle suspendedTownParticle = new WatchGrowthParticle(level, x, y, z, this.sprites, xSpeed, ySpeed, zSpeed);
            suspendedTownParticle.pickSprite(this.sprites);
            suspendedTownParticle.setColor(1.0F, 1.0F, 1.0F);
            return suspendedTownParticle;
        }
    }
}
