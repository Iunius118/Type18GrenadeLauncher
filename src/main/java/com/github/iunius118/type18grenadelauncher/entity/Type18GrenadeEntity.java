package com.github.iunius118.type18grenadelauncher.entity;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncher;
import com.github.iunius118.type18grenadelauncher.config.Type18GrenadeLauncherConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

public class Type18GrenadeEntity extends ThrowableEntity {
    public static final String NAME = "Grenade";
    public static final int FUSE_SAFETY = 1;
    public static final int FUSE_MAX = 160;
    public static final float DIRECT_DAMAGE = 40.0F;
    public static final float INITIAL_VELOCITY = 3.0F;
    public static final ResourceLocation ID = new ResourceLocation(Type18GrenadeLauncher.MOD_ID, NAME.toLowerCase());

    public static final String TAG_TICKS_AGE = "age";
    public static final String TAG_POWER = "power";
    public static final String TAG_THROWER = "owner";

    public int ticksAge = 0;
    public float power = Type18GrenadeLauncherConfig.COMMON.grenade40mm.explosivePower.get().floatValue();
    public String throwerName = "?";

    public Type18GrenadeEntity(FMLPlayMessages.SpawnEntity packet, World worldIn) {
        super(Type18GrenadeLauncher.EntityTypes.GRENADE, worldIn);
    }

    public Type18GrenadeEntity(LivingEntity throwerIn, World worldIn, float power) {
        super(Type18GrenadeLauncher.EntityTypes.GRENADE, throwerIn, worldIn);

        this.throwerName = throwerIn.getName().getString();
        this.power = power;
    }

    public Type18GrenadeEntity(EntityType<? extends Type18GrenadeEntity> type, World worldIn) {
        super(type, worldIn);

        if (worldIn.isRemote) {
            // Client side
            setRenderDistanceWeight(4.0F);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.isAlive()) {
            return;
        }

        ++this.ticksAge;

        if (Type18GrenadeLauncherConfig.COMMON.debug.get() && !this.world.isRemote) {
            // Server side
            this.printDebugLog();
        }

        if (this.ticksAge > FUSE_MAX) {
            // Time is up
            this.onImpact(new EntityRayTraceResult(this));
        } else {
            Vec3d motion = this.getMotion();
            Vec3d vecStart = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vecEnd = new Vec3d(this.posX + motion.x, this.posY + motion.y, this.posZ + motion.z);
            BlockRayTraceResult raytraceresult = this.world.rayTraceBlocks(new RayTraceContext(vecStart, vecEnd, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.ANY, this));

            if (raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
                // Hit liquid
                this.onImpact(raytraceresult);
            }
        }
    }

    @Override
    public void onImpact(RayTraceResult result) {
        if (!this.world.isRemote) {
            // Server side
            ServerWorld world = (ServerWorld) this.world;
            Vec3d hitVec = result.getHitVec();

            if (this.ticksAge > FUSE_SAFETY) {
                // Create explosion visual and sound effects
                spawnExplosionParticleFromServer(world, hitVec);
                world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);

                if (this.isPermittedDamage(DamageLevel.ENTITY) && power > 0.0F) {
                    // Create explosion
                    Explosion explosion = world.createExplosion(this.getThrower(), hitVec.x, hitVec.y + (double) (this.getHeight() / 2.0F), hitVec.z, this.power, this.isPermittedDamage(DamageLevel.TERRAIN) ? Explosion.Mode.BREAK : Explosion.Mode.NONE);
                }

                this.remove();
                this.logInfo("-Detonated", hitVec);

            } else {
                // Hit at close distance (in the very short time), deal direct damage
                if (this.isPermittedDamage(DamageLevel.ENTITY) && power > 0.0F) {
                    if (result instanceof EntityRayTraceResult) {
                        Entity entity = ((EntityRayTraceResult) result).getEntity();

                        if(entity instanceof PlayerEntity) {
                            entity.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) getThrower()), DIRECT_DAMAGE);

                        } else if(entity != null) {
                            entity.attackEntityFrom(DamageSource.causeMobDamage(getThrower()), DIRECT_DAMAGE);
                        }
                    }
                }

                this.remove();
                this.logInfo("-Hit", hitVec);
            }

        } else {
            // Client side
            if (result instanceof EntityRayTraceResult) {
                if (((EntityRayTraceResult) result).getEntity() == this) {
                    this.remove();
                }

            } else {
                this.remove();
            }
        }
    }

    private void spawnExplosionParticleFromServer(ServerWorld worldIn, Vec3d pos) {
        for(ServerPlayerEntity player: worldIn.getPlayers()) {
            worldIn.spawnParticle(player, ParticleTypes.EXPLOSION_EMITTER, true, pos.x, pos.y, pos.z, 1, 1.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void onKillCommand() {
        super.onKillCommand();

        this.logInfo("-KillCommand", new Vec3d(this.posX, this.posY, this.posZ));
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected float getGravityVelocity() {
        return super.getGravityVelocity();
    }


    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);

        compound.putInt(TAG_TICKS_AGE, this.ticksAge);
        compound.putFloat(TAG_POWER, this.power);
        compound.putString(TAG_THROWER, this.throwerName);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);

        this.ticksAge = compound.getInt(TAG_TICKS_AGE);
        this.power = compound.getFloat(TAG_POWER);
        this.throwerName = compound.getString(TAG_THROWER);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public float getEyeHeight(Pose pose) {
        return this.getHeight() * 0.5F;
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return distance < 65536;
    }

    @Override
    public Vec3d getLookVec() {
        return this.getMotion().normalize();
    }

    public enum DamageLevel {
        NONE,
        ENTITY,
        TERRAIN
    }

    public static boolean isPermittedDamage(DamageLevel level) {
        int permittedLevel = Type18GrenadeLauncherConfig.COMMON.grenadeDamageLevel.get();

        if (level.ordinal() <= permittedLevel) {
            return true;
        }

        return false;
    }

    public float getInitialVelocity() {
        return INITIAL_VELOCITY;
    }

    public void logInfo(String type, Vec3d pos) {
        if (Type18GrenadeLauncherConfig.COMMON.enableLog.get()) {
            Type18GrenadeLauncher.LOGGER.info("{} #{} [{}] at {}, launched by {}", NAME, this.getUniqueID(), type, pos.toString(), this.throwerName);
        }
    }

    public void logInfo(String type, Vec3d pos, Vec3d direction) {
        if (Type18GrenadeLauncherConfig.COMMON.enableLog.get()) {
            Type18GrenadeLauncher.LOGGER.info("{} #{} [{}] at {}, launched by {} for {}", NAME, this.getUniqueID(), type, pos.toString(), this.throwerName, direction.toString());
        }
    }

    public void printDebugLog() {
        Type18GrenadeLauncher.LOGGER.info("{}, T: {}, P: {}, V: {}, S: {}",
                NAME, this.ticksAge, this.getPositionVector().toString(), this.getMotion().length(), power
        );
    }
}
