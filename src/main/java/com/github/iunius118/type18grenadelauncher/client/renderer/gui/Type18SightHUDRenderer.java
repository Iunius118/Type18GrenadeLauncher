package com.github.iunius118.type18grenadelauncher.client.renderer.gui;

import com.github.iunius118.type18grenadelauncher.Type18GrenadeLauncherConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
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
        if (rangeIndicator == null && !createRangeIndicator()) {
            return;
        }

        EntityPlayer player = Minecraft.getMinecraft().player;

        if (!player.isSneaking()) {
            return;
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        FontRenderer fontRenderer = mc.fontRenderer;
        double width = scaledResolution.getScaledWidth_double();
        double height = scaledResolution.getScaledHeight_double();
        Entity viewEntity = mc.getRenderViewEntity();
        float pitch = viewEntity.prevRotationPitch + (viewEntity.rotationPitch - viewEntity.prevRotationPitch) * partialTicks;

        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        setColor(Type18GrenadeLauncherConfig.client.gunSight.color);
        GlStateManager.color(red, green, blue, alpha);
        GlStateManager.glLineWidth(1.0F);

        for (Pair<Double, String> index : rangeIndicator) {
            double angle = index.getLeft() + pitch;

            if (angle < -60.0D || angle > 60.0D) {
                continue;
            }

            double y = (1 - Math.tan(angle * Math.PI / 180.0D) * cotHalfFOV) * height / 2;

            GlStateManager.disableTexture2D();

            vertexBuffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
            vertexBuffer.pos(width * 0.4625D, y, 0.0D).endVertex();
            vertexBuffer.pos(width * 0.4875D, y, 0.0D).endVertex();
            vertexBuffer.pos(width * 0.5125D, y, 0.0D).endVertex();
            vertexBuffer.pos(width * 0.5375D, y, 0.0D).endVertex();
            tessellator.draw();

            GlStateManager.enableTexture2D();

            fontRenderer.drawString(index.getRight(), (float) width * 0.55F, (float) y - fontRenderer.FONT_HEIGHT / 2.0F, Type18GrenadeLauncherConfig.client.gunSight.color, false);
        }

        GlStateManager.enableDepth();
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

        double[] listAngles = Type18GrenadeLauncherConfig.client.gunSight.listAngles;
        String[] listRange = Type18GrenadeLauncherConfig.client.gunSight.listRange;
        int index = 0;

        for(double angle : listAngles) {
            if (angle < -90.0D || angle > 90.0D) {
                index++;
                continue;
            }

            String range = index < listRange.length ? listRange[index] : "";
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
