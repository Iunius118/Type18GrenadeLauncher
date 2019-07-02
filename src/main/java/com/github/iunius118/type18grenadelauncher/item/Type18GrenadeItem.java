package com.github.iunius118.type18grenadelauncher.item;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncher;
import com.github.iunius118.type18grenadelauncher.entity.Type18GrenadeEntity;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class Type18GrenadeItem extends Item {
    public static final ResourceLocation ID = new ResourceLocation(Type18GrenadeLauncher.MOD_ID, "type_18_grenade");

    public Type18GrenadeItem() {
        this.setCreativeTab(CreativeTabs.COMBAT);
    }

    public Type18GrenadeEntity getEntity(World world, EntityLivingBase thrower) {
        return new Type18GrenadeEntity(world, thrower);
    }
}
