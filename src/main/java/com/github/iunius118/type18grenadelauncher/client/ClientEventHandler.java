package com.github.iunius118.type18grenadelauncher.client;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncher;
import com.github.iunius118.type18grenadelauncher.client.renderer.entity.Type18GrenadeRenderer;
import com.github.iunius118.type18grenadelauncher.entity.Type18GrenadeEntity;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientEventHandler {
    @SubscribeEvent
    public void onModelRegistryEvent (ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(Type18GrenadeLauncher.Items.GRENADE_LAUNCHER, 0, new ModelResourceLocation(Type18GrenadeLauncher.Items.GRENADE_LAUNCHER.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Type18GrenadeLauncher.Items.GRENADE_LAUNCHER_REVOLVER, 0, new ModelResourceLocation(Type18GrenadeLauncher.Items.GRENADE_LAUNCHER_REVOLVER.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Type18GrenadeLauncher.Items.GRENADE_DISCHARGER, 0, new ModelResourceLocation(Type18GrenadeLauncher.Items.GRENADE_DISCHARGER.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Type18GrenadeLauncher.Items.GRENADE_40, 0, new ModelResourceLocation(Type18GrenadeLauncher.Items.GRENADE_40.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Type18GrenadeLauncher.Items.GRENADE_51, 0, new ModelResourceLocation(Type18GrenadeLauncher.Items.GRENADE_51.getRegistryName(), "inventory"));

        RenderingRegistry.registerEntityRenderingHandler(Type18GrenadeEntity.class, Type18GrenadeRenderer::new);
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Type18GrenadeLauncher.MOD_ID)) {
            ConfigManager.sync(Type18GrenadeLauncher.MOD_ID, Config.Type.INSTANCE);
        }
    }
}
