package com.github.iunius118.type18grenadelauncher.item;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncher;
import com.github.iunius118.type18grenadelauncher.config.Type18GrenadeLauncherConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

public class Type18GrenadeDischargerItem extends Type18GrenadeLauncherItem {
    public static final ResourceLocation ID = new ResourceLocation(Type18GrenadeLauncher.MOD_ID, "grenade_discharger");
    public static final float INACCURACY = 0.25F;
    public static final float MAX_INACCURACY = 4.0F;

    @Override
    public boolean isAmmo(@Nonnull ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() instanceof Type18Grenade51Item);
    }

    @Override
    public Item getDefaultAmmoItem() {
        return Type18GrenadeLauncher.Items.GRENADE_51;
    }

    @Override
    public boolean canLaunch(World worldIn, PlayerEntity playerIn, Hand handIn) {
        return super.canLaunch(worldIn, playerIn, handIn) && playerIn.isSneaking() && playerIn.onGround;
    }

    @Override
    public float getInaccuracy(World worldIn, PlayerEntity playerIn, Hand handIn) {
        float pitch = playerIn.rotationPitch;

        if (pitch < -45.0F) {
            return INACCURACY;
        } else if (pitch < 0.0F) {
            return (1.0F + pitch / 45.0F) * (MAX_INACCURACY - INACCURACY) + INACCURACY;
        } else {
            return MAX_INACCURACY;
        }
    }

    @Override
    public void playFiringSound(World worldIn, PlayerEntity playerIn, Hand handIn) {
        Vec3d soundPos = playerIn.getLook(1.0F).scale(2.0D).add(playerIn.posX, playerIn.posY + playerIn.getEyeHeight(), playerIn.posZ);
        worldIn.playSound(null, soundPos.x, soundPos.y, soundPos.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 0.5F, (1.0F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.2F) * 0.7F);

        if (worldIn instanceof ServerWorld) {
            ((ServerWorld) worldIn).spawnParticle(ParticleTypes.EXPLOSION, soundPos.x, soundPos.y, soundPos.z, 1, 1.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void coolDown(World worldIn, PlayerEntity playerIn, Hand handIn) {
        playerIn.getCooldownTracker().setCooldown(playerIn.getHeldItem(handIn).getItem(), Type18GrenadeLauncherConfig.COMMON.mortar51mm.reloadingCoolDown.get());
    }

    @Override
    public void recoil(World worldIn, PlayerEntity playerIn, Hand handIn) {
    }
}
