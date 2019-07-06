package com.github.iunius118.type18grenadelauncher.entity;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncher;
import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncherConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class Type18GrenadeEntity extends EntityThrowable {
    public static final String NAME = "Grenade";
    public static final int COLOR = 0xFF5E7C16;
    public static final int FUSE_SAFETY = 2;
    public static final int FUSE_MAX = 150;
    public static final float STRENGTH = 4.0F;
    public static final float DIRECT_DAMAGE = 40.0F;
    public static final float INITIAL_VELOCITY = 3.0F;
    public static final float INACCURACY = 1.0F;
    public static final ResourceLocation ID = new ResourceLocation(Type18GrenadeLauncher.MOD_ID, Type18GrenadeEntity.NAME.toLowerCase());

    public static final String TAG_TICKS_AGE = "age";
    public static final String TAG_THROWER = "age";

    public int ticksAge = 0;
    public String throwerName = "?";

    public Type18GrenadeEntity(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);

        this.ignoreEntity = throwerIn;
        this.throwerName = throwerIn.getName();
    }

    public Type18GrenadeEntity(World worldIn) {
        super(worldIn);

        if (worldIn.isRemote) {
            // Client side
            this.setRenderDistanceWeight(4.0F);
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.isDead) {
            return;
        }

        ++this.ticksAge;

        if (Type18GrenadeLauncher.DEBUG && !this.world.isRemote) {
            // Server side
            this.printDebugLog();
        }

        if (this.ticksAge > this.FUSE_MAX) {
            // Time is up
            this.onImpact(new RayTraceResult(this));
        } else {
            Vec3d vecStart = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vecEnd = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            RayTraceResult raytraceresult = this.world.rayTraceBlocks(vecStart, vecEnd, true);

            if (raytraceresult != null) {
                // Hit liquid
                this.onImpact(raytraceresult);
            }
        }
    }

    @Override
    public void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            // Server side
            WorldServer world = (WorldServer) this.world;

            if (this.ticksAge > this.FUSE_SAFETY) {
                // Create explosion visual and sound effects
                world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, true, result.hitVec.x, result.hitVec.y, result.hitVec.z, 1, 0.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);

                if (this.isPermittedDamage(DamageLavel.ENTITY)) {
                    // Create explosion
                    Explosion explosion = world.createExplosion(this.getThrower(), result.hitVec.x, result.hitVec.y + (double) (this.height / 2.0F), result.hitVec.z, this.STRENGTH, this.isPermittedDamage(DamageLavel.TERRAIN));
                }

                this.setDead();
                this.logInfo("-Detonated", result.hitVec);

            } else {
                // Hit at close distance (in the very short time), deal direct damage
                if (this.isPermittedDamage(DamageLavel.ENTITY)) {
                    Entity entity = result.entityHit;

                    if (entity instanceof EntityPlayer) {
                        entity.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) this.getThrower()), this.DIRECT_DAMAGE);

                    } else if (entity != null) {
                        entity.attackEntityFrom(DamageSource.causeMobDamage(this.getThrower()), this.DIRECT_DAMAGE);
                    }
                }

                this.setDead();
                this.logInfo("-Hit", result.hitVec);
            }

        } else {
            // Client side
            if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
                if (result.entityHit == this) {
                    this.setDead();
                }

            } else {
                this.setDead();
            }
        }
    }

    @Override
    public void onKillCommand() {
        super.onKillCommand();

        this.logInfo("-KillCommand", new Vec3d(this.posX, this.posY, this.posZ));
    }

    @Override
    protected float getGravityVelocity() {
        return super.getGravityVelocity();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        compound.setInteger(this.TAG_TICKS_AGE, this.ticksAge);
        compound.setString(this.TAG_THROWER, this.throwerName);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        this.ticksAge = compound.getInteger(this.TAG_TICKS_AGE);
        this.throwerName = compound.getString(this.TAG_THROWER);
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.5F;
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return distance < 65536;
    }

    @Override
    public Vec3d getLook(float partialTicks) {
        return new Vec3d(this.motionX, this.motionY, this.motionZ);
    }

    public enum DamageLavel {
        NONE,
        ENTITY,
        TERRAIN
    }

    public boolean isPermittedDamage(DamageLavel level) {
        int permittedLevel = Type18GrenadeLauncherConfig.common.grenadeDamageLevel;

        if (level.ordinal() <= permittedLevel) {
            return true;
        }

        return false;
    }

    public void logInfo(String type, Vec3d pos) {
        if (Type18GrenadeLauncher.config.common.enableLog) {
            Type18GrenadeLauncher.logger.info("{} #{} [{}] at {}, launched by {}", NAME, this.getUniqueID(), type, pos.toString(), this.throwerName);
        }
    }

    public void logInfo(String type, Vec3d pos, Vec3d direction) {
        if (Type18GrenadeLauncher.config.common.enableLog) {
            Type18GrenadeLauncher.logger.info("{} #{} [{}] at {}, launched by {} for {}", NAME, this.getUniqueID(), type, pos.toString(), this.throwerName, direction.toString());
        }
    }

    public void printDebugLog() {
        Type18GrenadeLauncher.logger.info("{}, T: {}, P: {}, V: {}",
                this.NAME, this.ticksAge, this.getPositionVector().toString(), this.motionY, Math.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ)
        );
    }
}
