package com.github.iunius118.type18grenadelauncher.item;

import com.github.iunius118.type18grenadelauncher.entity.Type18GrenadeEntity;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class Type18GrenadeItem extends Item {
    public Type18GrenadeItem() {
        this.setCreativeTab(CreativeTabs.COMBAT);
    }

    public Type18GrenadeEntity getEntity(World world, EntityLivingBase thrower) {
        return new Type18GrenadeEntity(world, thrower);
    }
}
