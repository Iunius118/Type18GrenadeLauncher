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
    public static final int COOL_DOWN = 40;

    @Override
    public int getCoolDownTime() {
        return COOL_DOWN;
    }

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
        return playerIn.isSneaking();
    }

    @Override
    public void recoil(EntityPlayer playerIn, EnumHand handIn) {
    }
}
