package com.github.iunius118.type18grenadelauncher.client.renderer.entity;

import com.github.iunius118.type18grenadelauncher.entity.Type18GrenadeEntity;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class Type18GrenadeRenderer extends EntityRenderer<Type18GrenadeEntity> {
    public Type18GrenadeRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(Type18GrenadeEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        ItemModelMesher mesher = Minecraft.getInstance().getItemRenderer().getItemModelMesher();
        IBakedModel model = mesher.getItemModel(new ItemStack(Blocks.WHITE_TERRACOTTA));

        this.bindEntityTexture(entity);

        // Transform
        // float spin = (entity.ticksAge + partialTicks) * 30 % 360;
        GlStateManager.pushMatrix();
        GlStateManager.translated( x, y + 0.125D, z);
        GlStateManager.rotatef(entityYaw - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks - 90.0F, 0.0F, 0.0F, 1.0F);
        // GlStateManager.rotatef(spin, 0.0F, 1.0F, 0.0F);
        GlStateManager.scalef(0.2F, 0.25F, 0.2F);
        GlStateManager.translatef(-0.5F, -1.0F, -0.5F);

        Random random = new Random();

        // Draw faces except bottom
        for (Direction face : Direction.values()) {
            if (face == Direction.DOWN) {
                continue;
            }

            vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

            List<BakedQuad> quads = model.getQuads(null, face, random, EmptyModelData.INSTANCE);
            quads.forEach(quad -> LightUtil.renderQuadColor(vertexBuffer, quad, 0xFF5E7C16));

            tessellator.draw();
        }

        // Draw bottom face (tracer)
        GlStateManager.disableLighting();

        // Tweak lightmap to draw as bright
        float lastBrightnessX = GLX.lastBrightnessX;
        float lastBrightnessY = GLX.lastBrightnessY;
        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);

        vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

        List<BakedQuad> quads = model.getQuads(null, Direction.DOWN, random, EmptyModelData.INSTANCE);
        quads.forEach(quad -> LightUtil.renderQuadColor(vertexBuffer, quad, 0xFF1D1D21));

        tessellator.draw();

        // Draw tracer point
        GlStateManager.disableTexture();

        GL11.glPointSize(1.5F);
        vertexBuffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);
        vertexBuffer.pos(0.5F, -0.1F, 0.5F).color(255, 64, 64, 255).endVertex();
        tessellator.draw();
        GL11.glPointSize(1.0F);

        GlStateManager.enableTexture();

        // Restore lightmap
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, lastBrightnessX, lastBrightnessY);
        GL11.glPopAttrib();

        GlStateManager.enableLighting();

        GlStateManager.popMatrix();
    }

    @Override
    @Nullable
    protected ResourceLocation getEntityTexture(Type18GrenadeEntity entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}
