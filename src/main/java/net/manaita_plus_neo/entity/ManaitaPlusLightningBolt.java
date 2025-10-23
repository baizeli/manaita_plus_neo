package net.manaita_plus_neo.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ManaitaPlusLightningBolt extends Entity {
    public float f;
    private int life;
    public long seed;
    private int flashes;

    public ManaitaPlusLightningBolt(EntityType<? extends ManaitaPlusLightningBolt> p_20865_, Level p_20866_) {
        super(p_20865_, p_20866_);
        this.noCulling = true;
        this.life = 2;
        this.seed = this.random.nextLong();
        this.flashes = this.random.nextInt(3) + 1;
    }

    public @NotNull SoundSource getSoundSource() {
        return SoundSource.WEATHER;
    }

    public void tick() {
        super.tick();
        if (this.life == 2) {
            if (this.level().isClientSide()) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.WEATHER, 10000.0F, 0.8F + this.random.nextFloat() * 0.2F, false);
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.WEATHER, 2.0F, 0.5F + this.random.nextFloat() * 0.2F, false);
            }
        }

        --this.life;
        if (this.life < 0) {
            if (this.flashes == 0) {
                this.discard();
            } else if (this.life < -this.random.nextInt(10)) {
                --this.flashes;
                this.life = 1;
                this.seed = this.random.nextLong();
            }
        }

        if (this.life >= 0) {
            if (!(this.level() instanceof ServerLevel)) {
                this.level().setSkyFlashTime(2);
            }
        }

    }

    public boolean shouldRenderAtSqrDistance(double p_20869_) {
        double d0 = 64.0D * getViewScale();
        return p_20869_ < d0 * d0;
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    protected void readAdditionalSaveData(CompoundTag p_20873_) {
    }

    protected void addAdditionalSaveData(CompoundTag p_20877_) {
    }
}