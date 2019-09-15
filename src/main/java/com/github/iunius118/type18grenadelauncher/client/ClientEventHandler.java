package com.github.iunius118.type18grenadelauncher.client;

import com.github.iunius118.type18grenadelauncher.client.renderer.gui.Type18SightHUDRenderer;
import com.github.iunius118.type18grenadelauncher.config.Type18GrenadeLauncherConfig;
import com.github.iunius118.type18grenadelauncher.item.Type18GrenadeLauncherItem;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class ClientEventHandler {
    private final Type18SightHUDRenderer sightHUDRenderer = new Type18SightHUDRenderer();
    private static final FloatBuffer matrixBuf = BufferUtils.createFloatBuffer(16);

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        matrixBuf.clear();
        GlStateManager.getMatrix(GL11.GL_PROJECTION_MATRIX, matrixBuf);
        sightHUDRenderer.setCotHalfFOV(matrixBuf.get(5));
    }

    @SubscribeEvent
    public void onRenderGameOverlayEventPre(RenderGameOverlayEvent.Pre event) {
        if (Type18GrenadeLauncherConfig.CLIENT.enableHUD.get()) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
                GameSettings options = Minecraft.getInstance().getRenderManager().options;

                if (options != null && options.thirdPersonView < 1 && isHoldingGrenadeLauncher()) {
                    sightHUDRenderer.doRenderer(event.getPartialTicks());
                }
            }
        }
    }

    private boolean isHoldingGrenadeLauncher() {
        PlayerEntity player = Minecraft.getInstance().player;

        if (player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof Type18GrenadeLauncherItem) {
            return true;
        } else {
            return player.getHeldItem(Hand.OFF_HAND).getItem() instanceof Type18GrenadeLauncherItem;
        }
    }
}
