package com.github.iunius118.type18grenadelauncher.entity;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncher;
import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncherConfig;
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
    public static final int FUSE_SAFETY = 2;
    public static final int FUSE_MAX = 80;
    public static final float STRENGTH = 4.0F;
    public static final float DIRECT_DAMAGE = 40.0F;
    public static final float INITIAL_VELOCITY = 4.0F;
    public static final float INACCURACY = 1.0F;
    public static final ResourceLocation ID = new ResourceLocation(Type18GrenadeLauncher.MOD_ID, Type18GrenadeEntity.NAME.toLowerCase());

    public static final String TAG_TICKS_AGE = "age";

    public int ticksAge = 0;
    public boolean canDropIronIngot = true;

    public Type18GrenadeEntity(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
        this.ignoreEntity = throwerIn;
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

        ++this.ticksAge;

        if (!this.world.isRemote) {
            // Server side
            this.printDebugLog();
        } else {
            // Client side
            this.world.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, true, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
        }

        if (!this.isDead && (this.ticksAge > this.FUSE_MAX || this.isInWater() || this.isInLava())) {
            // Time is up or Hit water/lava
            this.onImpact(new RayTraceResult(this));
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            // Server side
            WorldServer world = (WorldServer) this.world;

            if (this.ticksAge > this.FUSE_SAFETY) {
                // Create explosion visual and sound effects
                world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, true, result.hitVec.x, result.hitVec.y, result.hitVec.z, 1, 0.0D, 0.0D, 0.0D, 0.0D, new int[0]);
                world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);

                if (this.isPermittedDamage(DamageLavel.ENTITY)) {
                    // Create explosion
                    Explosion explosion = world.createExplosion(this.getThrower(), result.hitVec.x, result.hitVec.y, result.hitVec.z, this.STRENGTH, this.isPermittedDamage(DamageLavel.TERRAIN));
                }
            } else {
                // Hit at close distance (in the very short time), deal direct damage
                if (this.isPermittedDamage(DamageLavel.ENTITY)) {
                    if (result.entityHit instanceof EntityPlayer) {
                        result.entityHit.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) this.getThrower()), this.DIRECT_DAMAGE);
                    } else {
                        result.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.getThrower()), this.DIRECT_DAMAGE);
                    }
                }
            }
        }

        this.setDead();
        this.logOnDead("onImpact", result.hitVec);
    }

    @Override
    public void onKillCommand() {
        super.onKillCommand();

        this.logOnDead("onKillCommand", new Vec3d(this.posX, this.posY, this.posZ));
    }

    public void logOnDead(String string, Vec3d pos) {
        if (!Type18GrenadeLauncher.config.common.enableLog) {
            return;
        }

        EntityLivingBase entity = this.thrower;
        String playerName = "???";

        if (entity instanceof EntityPlayer) {
            playerName =  entity.getName();
        }

        Type18GrenadeLauncher.logger.info(
                NAME
                + " [" + string + "]"
                + " at " + pos.toString()
                + " launched by " + playerName
        );
    }

    @Override
    protected float getGravityVelocity() {
        return 0.0F;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        compound.setInteger(this.TAG_TICKS_AGE, this.ticksAge);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        this.ticksAge = compound.getInteger(this.TAG_TICKS_AGE);
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

    public void printDebugLog()
    {
        System.out.println(
                "Name: " + this.NAME
                + ", T: " + this.ticksAge
                + ", Px: " + this.posX
                + ", Py: " + this.posY
                + ", Pz: " + this.posZ
                + ", Vy: " + this.motionY
                + ", V: " + Math.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ)
        );
    }
}
