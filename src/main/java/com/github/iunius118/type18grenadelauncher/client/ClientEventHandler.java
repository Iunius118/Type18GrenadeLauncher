package com.github.iunius118.type18grenadelauncher.client;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncher;
import com.github.iunius118.type18grenadelauncher.client.renderer.entity.Type18GrenadeRenderer;
import com.github.iunius118.type18grenadelauncher.entity.Type18GrenadeEntity;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientEventHandler {
    @SubscribeEvent
    public void onModelRegistryEvent (ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(Type18GrenadeLauncher.Items.TYPE_18_GRENADE_LAUNCHER, 0, new ModelResourceLocation(Type18GrenadeLauncher.Items.TYPE_18_GRENADE_LAUNCHER.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Type18GrenadeLauncher.Items.TYPE_18_GRENADE, 0, new ModelResourceLocation(Type18GrenadeLauncher.Items.TYPE_18_GRENADE.getRegistryName(), "inventory"));

        RenderingRegistry.registerEntityRenderingHandler(Type18GrenadeEntity.class, Type18GrenadeRenderer::new);
    }
}
