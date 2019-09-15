package com.github.iunius118.type18grenadelauncher.client.renderer.gui;

import com.github.iunius118.type18grenadelauncher.config.Type18GrenadeLauncherConfig;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class Type18SightHUDRenderer {
    private List<Pair<Double, String>> rangeIndicator;
    private float cotHalfFOV = 1.428148F;  // = 1 / tan 35°
    private float red = 1.0F;
    private float green = 1.0F;
    private float blue = 1.0F;
    private float alpha = 1.0F;

    public void doRenderer(float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;

        if (!player.isSneaking() || !createRangeIndicator()) {
            return;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        FontRenderer fontRenderer = mc.fontRenderer;
        double width = mc.mainWindow.getScaledWidth();
        double height = mc.mainWindow.getScaledHeight();
        Entity viewEntity = mc.getRenderViewEntity();
        float pitch = viewEntity.prevRotationPitch + (viewEntity.rotationPitch - viewEntity.prevRotationPitch) * partialTicks;
        int hudColor = Type18GrenadeLauncherConfig.CLIENT.sightHUD.color.get();

        GlStateManager.disableDepthTest();
        GlStateManager.enableBlend();
        setColor(hudColor);
        GlStateManager.color4f(red, green, blue, alpha);
        GlStateManager.lineWidth(1.0F);

        for (Pair<Double, String> index : rangeIndicator) {
            double angle = index.getLeft() + pitch;

            if (angle < -60.0D || angle > 60.0D) {
                continue;
            }

            double y = (1 - Math.tan(angle * Math.PI / 180.0D) * cotHalfFOV) * height / 2;

            GlStateManager.disableTexture();

            vertexBuffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
            vertexBuffer.pos(width * 0.4625D, y, 0.0D).endVertex();
            vertexBuffer.pos(width * 0.4875D, y, 0.0D).endVertex();
            vertexBuffer.pos(width * 0.5125D, y, 0.0D).endVertex();
            vertexBuffer.pos(width * 0.5375D, y, 0.0D).endVertex();
            tessellator.draw();

            GlStateManager.enableTexture();

            fontRenderer.drawString(index.getRight(), (float) width * 0.55F, (float) y - fontRenderer.FONT_HEIGHT / 2.0F, hudColor);
        }

        GlStateManager.enableDepthTest();
    }

    private void setColor(int color) {
        int nColor = color;
        blue = (float) (nColor & 0xFF) / 255.0F;
        nColor >>= 8;
        green = (float) (nColor & 0xFF) / 255.0F;
        nColor >>= 8;
        red = (float) (nColor & 0xFF) / 255.0F;
        nColor >>= 8;
        alpha = (float) (nColor & 0xFF) / 255.0F;
    }

    public void clearRangeIndicator() {
        rangeIndicator = null;
    }

    private boolean createRangeIndicator() {
        List<Pair<Double, String>> list = new ArrayList<>();

        List<Double> listAngles = Type18GrenadeLauncherConfig.CLIENT.sightHUD.angleList.get();
        List<String> listRange = Type18GrenadeLauncherConfig.CLIENT.sightHUD.rangeList.get();
        int index = 0;

        for(double angle : listAngles) {
            if (angle < -90.0D || angle > 90.0D) {
                index++;
                continue;
            }

            String range = index < listRange.size() ? listRange.get(index) : "";
            list.add(Pair.of(angle, range));
            index++;
        }

        if (!list.isEmpty()) {
            rangeIndicator = list;
            return true;
        }

        return false;
    }

    public void setCotHalfFOV(float f) {
        cotHalfFOV = f;
    }
}
