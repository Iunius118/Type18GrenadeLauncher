package com.github.iunius118.type18grenadelauncher.client;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncher;
import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncherConfig;
import com.github.iunius118.type18grenadelauncher.client.renderer.entity.Type18GrenadeRenderer;
import com.github.iunius118.type18grenadelauncher.client.renderer.gui.Type18SightHUDRenderer;
import com.github.iunius118.type18grenadelauncher.entity.Type18GrenadeEntity;
import com.github.iunius118.type18grenadelauncher.item.Type18GrenadeLauncherItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class ClientEventHandler {
    private final Type18SightHUDRenderer sightHUDRenderer = new Type18SightHUDRenderer();

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
            sightHUDRenderer.clearRangeIndicator();
        }
    }

    private static final FloatBuffer matrixBuf = BufferUtils.createFloatBuffer(16);

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        matrixBuf.clear();
        GlStateManager.getFloat(GL11.GL_PROJECTION_MATRIX, matrixBuf);
        sightHUDRenderer.setCotHalfFOV(matrixBuf.get(5));
    }

    @SubscribeEvent
    public void onRenderGameOverlayEventPre(RenderGameOverlayEvent.Pre event) {
        if (!Type18GrenadeLauncherConfig.client.disableHUD) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
                GameSettings options = Minecraft.getMinecraft().getRenderManager().options;

                if (options != null && options.thirdPersonView < 1 && isHoldingGrenadeLauncher()) {
                    sightHUDRenderer.doRenderer(event.getPartialTicks());
                }
            }
        }
    }

    private boolean isHoldingGrenadeLauncher() {
        EntityPlayer player = Minecraft.getMinecraft().player;

        if (player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof Type18GrenadeLauncherItem) {
            return true;
        } else {
            return player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof Type18GrenadeLauncherItem;
        }
    }
}
