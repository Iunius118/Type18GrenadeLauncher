package com.github.iunius118.type18grenadelauncher.item;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class Type18GrenadeDischargerItem extends Type18GrenadeLauncherItem {
    public static final ResourceLocation ID = new ResourceLocation(Type18GrenadeLauncher.MOD_ID, "grenade_discharger");
    public static final float INACCURACY = 0.25F;
    public static final float MAX_INACCURACY = 4.0F;
    public static final int COOL_DOWN = 40;

    @Override
    public boolean isAmmo(@Nonnull ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() instanceof Type18Grenade51Item);
    }

    @Override
    public Item getDefaultAmmoItem() {
        return Type18GrenadeLauncher.Items.GRENADE_51;
    }

    @Override
    public boolean canLaunch(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        return playerIn.isSneaking() && playerIn.onGround;
    }

    @Override
    public float getInaccuracy(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
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
    public void coolDown(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        playerIn.getCooldownTracker().setCooldown(playerIn.getHeldItem(handIn).getItem(), COOL_DOWN);
    }

    @Override
    public void recoil(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
    }
}
