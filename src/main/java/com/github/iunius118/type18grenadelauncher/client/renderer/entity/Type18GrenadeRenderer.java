package com.github.iunius118.type18grenadelauncher.client.renderer.entity;

import com.github.iunius118.type18grenadelauncher.entity.Type18GrenadeEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.pipeline.LightUtil;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.List;

public class Type18GrenadeRenderer extends Render<Type18GrenadeEntity> {
    private IBakedModel model;

    public Type18GrenadeRenderer(RenderManager renderManager) {
        super(renderManager);

        ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
        model = mesher.getItemModel(new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.WHITE.getMetadata()));
    }

    @Override
    public void doRender(Type18GrenadeEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();

        this.bindEntityTexture(entity);

        // Transform
        // float spin = (entity.ticksAge + partialTicks) * 30 % 360;
        GlStateManager.pushMatrix();
        GlStateManager.translate( x, y + 0.125D, z);
        GlStateManager.rotate(entityYaw - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks - 90.0F, 0.0F, 0.0F, 1.0F);
        // GlStateManager.rotate(spin, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(0.2F, 0.25F, 0.2F);
        GlStateManager.translate(-0.5F, -1.0F, -0.5F);

        // Draw faces except bottom
        for (EnumFacing face : EnumFacing.VALUES) {
            if (face == EnumFacing.DOWN) {
                continue;
            }

            vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

            List<BakedQuad> quads = model.getQuads(null, face, 0L);
            quads.forEach(quad -> LightUtil.renderQuadColor(vertexBuffer, quad, 0xFF5E7C16));

            tessellator.draw();
        }

        // Draw bottom face (tracer)
        GlStateManager.disableLighting();

        // Tweak lightmap to draw as bright
        float lastBrightnessX = OpenGlHelper.lastBrightnessX;
        float lastBrightnessY = OpenGlHelper.lastBrightnessY;
        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);

        vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.ITEM);

        List<BakedQuad> quads = model.getQuads(null, EnumFacing.DOWN, 0L);
        quads.forEach(quad -> LightUtil.renderQuadColor(vertexBuffer, quad, 0xFF1D1D21));

        tessellator.draw();

        // Draw tracer point
        GlStateManager.disableTexture2D();

        GL11.glPointSize(1.5F);
        vertexBuffer.begin(GL11.GL_POINTS, DefaultVertexFormats.POSITION_COLOR);
        vertexBuffer.pos(0.5F, -0.1F, 0.5F).color(255, 64, 64, 255).endVertex();
        tessellator.draw();
        GL11.glPointSize(1.0F);

        GlStateManager.enableTexture2D();

        // Restore lightmap
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
        GL11.glPopAttrib();

        GlStateManager.enableLighting();

        GlStateManager.popMatrix();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(Type18GrenadeEntity entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
