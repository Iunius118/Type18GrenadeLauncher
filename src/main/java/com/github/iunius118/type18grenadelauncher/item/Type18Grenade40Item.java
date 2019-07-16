package com.github.iunius118.type18grenadelauncher.item;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncher;
import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncherConfig;
import com.github.iunius118.type18grenadelauncher.entity.Type18GrenadeEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class Type18Grenade40Item extends Type18GrenadeItem {
    public static final ResourceLocation ID = new ResourceLocation(Type18GrenadeLauncher.MOD_ID, "grenade_40");

    public Type18Grenade40Item() {
        super();
    }

    @Override
    public Type18GrenadeEntity getEntity(World world, EntityLivingBase thrower) {
        return new Type18GrenadeEntity(world, thrower, (float) Type18GrenadeLauncherConfig.common.grenade40mm.explosivePower);
    }
}
