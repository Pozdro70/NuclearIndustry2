package com.pozdro.nuclearindustry.block.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidRenderHelper {

    /**
     * Draws a tiled, correctly-colored fluid texture at the given screen position,
     * clipped to width x height pixels — like a scissor-tiled sprite.
     */
    public static void drawFluid(int x, int y, int width, int height, FluidStack fluidStack) {
        Fluid fluid = fluidStack.getFluid();
        if (fluid == null) return;

        // 1. Get the fluid's "still" sprite from the block texture atlas
        TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks()
                .getAtlasSprite(fluid.getStill(fluidStack).toString());

        // 2. Bind the BLOCK atlas, not your GUI texture
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        // 3. Apply the fluid's tint color (water is tinted blue from a mostly-white
        //    texture; lava/most fluids return 0xFFFFFFFF meaning "no tint needed")
        int color = fluid.getColor(fluidStack);
        float a = ((color >> 24) & 0xFF) / 255F;
        float r = ((color >> 16) & 0xFF) / 255F;
        float g = ((color >> 8) & 0xFF) / 255F;
        float b = (color & 0xFF) / 255F;
        if (a == 0F) a = 1F; // most fluids don't set alpha; treat 0 as "opaque"
        GlStateManager.color(r, g, b, a);

        GlStateManager.enableBlend();
        GlStateManager.disableLighting();

        // 4. Tile the 16x16 sprite across the target width/height
        int tileSize = 16;
        Tessellator tess = Tessellator.getInstance();
        BufferBuilder buffer = tess.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);

        for (int drawnY = 0; drawnY < height; drawnY += tileSize) {
            int tileHeight = Math.min(tileSize, height - drawnY);
            for (int drawnX = 0; drawnX < width; drawnX += tileSize) {
                int tileWidth = Math.min(tileSize, width - drawnX);

                float minU = sprite.getMinU();
                float maxU = sprite.getInterpolatedU(16 * tileWidth / (float) tileSize);
                float minV = sprite.getMinV();
                float maxV = sprite.getInterpolatedV(16 * tileHeight / (float) tileSize);

                int drawX = x + drawnX;
                int drawY = y + drawnY;

                buffer.pos(drawX, drawY + tileHeight, 0).tex(minU, maxV).endVertex();
                buffer.pos(drawX + tileWidth, drawY + tileHeight, 0).tex(maxU, maxV).endVertex();
                buffer.pos(drawX + tileWidth, drawY, 0).tex(maxU, minV).endVertex();
                buffer.pos(drawX, drawY, 0).tex(minU, minV).endVertex();
            }
        }

        tess.draw();

        GlStateManager.color(1F, 1F, 1F, 1F); // reset tint so it doesn't bleed into later draws
        GlStateManager.disableBlend();
    }
}