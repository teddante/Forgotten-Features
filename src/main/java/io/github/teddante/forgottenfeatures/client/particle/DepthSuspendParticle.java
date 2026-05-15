package io.github.teddante.forgottenfeatures.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public final class DepthSuspendParticle extends SingleQuadParticle {
    private final float initialAlpha;

    private DepthSuspendParticle(
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed,
            SpriteSet sprites,
            RandomSource random
    ) {
        super(level, x, y, z, xSpeed * 0.01D, ySpeed * 0.01D, zSpeed * 0.01D, sprites.get(random));
        this.friction = 0.98F;
        this.gravity = 0.0F;
        this.hasPhysics = false;
        this.lifetime = 45 + random.nextInt(45);
        this.quadSize = 0.035F + random.nextFloat() * 0.025F;
        this.rCol = 0.34F + random.nextFloat() * 0.08F;
        this.gCol = this.rCol;
        this.bCol = this.rCol;
        this.initialAlpha = 0.55F + random.nextFloat() * 0.20F;
        this.setAlpha(this.initialAlpha);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        this.move(this.xd, this.yd, this.zd);
        this.xd *= this.friction;
        this.yd *= this.friction;
        this.zd *= this.friction;

        float remaining = 1.0F - (float) this.age / (float) this.lifetime;
        this.setAlpha(this.initialAlpha * Math.min(1.0F, remaining * 2.0F));
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.TRANSLUCENT;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(
                SimpleParticleType type,
                ClientLevel level,
                double x,
                double y,
                double z,
                double xSpeed,
                double ySpeed,
                double zSpeed,
                RandomSource random
        ) {
            return new DepthSuspendParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites, random);
        }
    }
}
