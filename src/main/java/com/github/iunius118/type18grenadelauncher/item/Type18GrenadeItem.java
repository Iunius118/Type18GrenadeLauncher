package com.github.iunius118.type18grenadelauncher.item;

import com.github.iunius118.type18grenadelauncher.entity.Type18GrenadeEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.world.World;

public abstract class Type18GrenadeItem extends Item {
    public static Item.Properties properties = (new Item.Properties()).setNoRepair().group(ItemGroup.COMBAT);

    public Type18GrenadeItem() {
        super(properties);
    }

    public abstract Type18GrenadeEntity getEntity(World world, LivingEntity thrower);
}
