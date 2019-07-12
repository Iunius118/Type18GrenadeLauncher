package com.github.iunius118.type18grenadelauncher.item;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class Type18RevolverGrenadeLauncherItem extends Type18GrenadeLauncherItem {
    public static final ResourceLocation ID = new ResourceLocation(Type18GrenadeLauncher.MOD_ID, "grenade_launcher_revolver");
    public static final int COOL_DOWN = 10;
    public static final int COOL_DOWN_RELOAD = 250;
    public static final int MAX_SHOT_COUNT = 6;

    public static final String TAG_SHOT_COUNT = "shot";

    @Override
    public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        super.onCreated(stack, worldIn, playerIn);

        playerIn.getCooldownTracker().setCooldown(stack.getItem(), COOL_DOWN_RELOAD);
    }

    @Override
    public void coolDown(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        NBTTagCompound tag = stack.getTagCompound();

        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }

        int shotCount = tag.getInteger(TAG_SHOT_COUNT) + 1;

        if (shotCount >= MAX_SHOT_COUNT) {
            playerIn.getCooldownTracker().setCooldown(stack.getItem(), COOL_DOWN_RELOAD);
            tag.setInteger(TAG_SHOT_COUNT, 0);
        } else {
            playerIn.getCooldownTracker().setCooldown(stack.getItem(), COOL_DOWN);
            tag.setInteger(TAG_SHOT_COUNT, shotCount);
        }
    }

    @Override
    public void recoil(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        float recoilPitch = playerIn.getRNG().nextFloat() * -2.0F;
        playerIn.rotationPitch += recoilPitch;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            double shotCount = (double) tag.getInteger(TAG_SHOT_COUNT);
            return shotCount / (double) MAX_SHOT_COUNT;
        }

        return 0.0D;
    }
}
