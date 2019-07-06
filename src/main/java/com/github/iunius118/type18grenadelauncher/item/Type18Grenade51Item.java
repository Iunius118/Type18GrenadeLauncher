package com.github.iunius118.type18grenadelauncher.item;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncher;
import com.github.iunius118.type18grenadelauncher.entity.Type18GrenadeEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class Type18Grenade51Item extends Type18GrenadeItem {
    public static final ResourceLocation ID = new ResourceLocation(Type18GrenadeLauncher.MOD_ID, "type_18_grenade_51");
    public static final float POWER = 5.0F;

    public Type18Grenade51Item() {
        super();
    }

    @Override
    public Type18GrenadeEntity getEntity(World world, EntityLivingBase thrower) {
        return new Type18GrenadeEntity(world, thrower, POWER);
    }
}
