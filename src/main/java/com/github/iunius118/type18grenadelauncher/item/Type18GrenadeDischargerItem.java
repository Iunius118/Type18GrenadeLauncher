package com.github.iunius118.type18grenadelauncher.item;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class Type18GrenadeDischargerItem extends Type18GrenadeLauncherItem {
    public static final ResourceLocation ID = new ResourceLocation(Type18GrenadeLauncher.MOD_ID, "type_18_grenade_discharger");
    public static final int COOL_DOWN = 40;

    @Override
    public int getCoolDownTime() {
        return this.COOL_DOWN;
    }

    @Override
    public boolean canLaunch(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        return playerIn.getCooldownTracker().getCooldown(stack.getItem(), 0.0F) == 0.0F && playerIn.isSneaking();
    }
}
