package com.github.iunius118.type18grenadelauncher.item;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncher;
import com.github.iunius118.type18grenadelauncher.config.Type18GrenadeLauncherConfig;
import com.github.iunius118.type18grenadelauncher.entity.Type18GrenadeEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class Type18Grenade51Item extends Type18GrenadeItem {
    public static final ResourceLocation ID = new ResourceLocation(Type18GrenadeLauncher.MOD_ID, "grenade_51");

    public Type18Grenade51Item() {
        super();
    }

    @Override
    public Type18GrenadeEntity getEntity(World world, LivingEntity thrower) {
        return new Type18GrenadeEntity(thrower, world, Type18GrenadeLauncherConfig.COMMON.grenade51mm.explosivePower.get().floatValue());
    }
}
