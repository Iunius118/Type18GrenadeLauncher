package com.github.iunius118.type18grenadelauncher.item;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncher;
import com.github.iunius118.type18grenadelauncher.config.Type18GrenadeLauncherConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class Type18RevolverGrenadeLauncherItem extends Type18GrenadeLauncherItem {
    public static final ResourceLocation ID = new ResourceLocation(Type18GrenadeLauncher.MOD_ID, "grenade_launcher_revolver");
    public static final int MAX_SHOT_COUNT = 6;

    public static final String TAG_SHOT_COUNT = "shot";

    @Override
    public void onCreated(ItemStack stack, World worldIn, PlayerEntity playerIn) {
        super.onCreated(stack, worldIn, playerIn);

        playerIn.getCooldownTracker().setCooldown(stack.getItem(), Type18GrenadeLauncherConfig.COMMON.launcher40mmRevolver.reloadingCoolDown.get());
    }

    @Override
    public void coolDown(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        CompoundNBT tag = stack.getOrCreateTag();

        int shotCount = tag.getInt(TAG_SHOT_COUNT) + 1;

        if (shotCount >= MAX_SHOT_COUNT) {
            playerIn.getCooldownTracker().setCooldown(stack.getItem(), Type18GrenadeLauncherConfig.COMMON.launcher40mmRevolver.reloadingCoolDown.get());
            tag.putInt(TAG_SHOT_COUNT, 0);
        } else {
            playerIn.getCooldownTracker().setCooldown(stack.getItem(), Type18GrenadeLauncherConfig.COMMON.launcher40mmRevolver.firingCoolDown.get());
            tag.putInt(TAG_SHOT_COUNT, shotCount);
        }
    }

    @Override
    public void recoil(World worldIn, PlayerEntity playerIn, Hand handIn) {
        float recoilPitch = playerIn.getRNG().nextFloat() * -2.0F;
        playerIn.rotationPitch += recoilPitch;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        CompoundNBT tag = stack.getOrCreateTag();
        double shotCount = (double) tag.getInt(TAG_SHOT_COUNT);
        return shotCount / (double) MAX_SHOT_COUNT;
    }
}
