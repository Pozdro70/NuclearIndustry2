package com.pozdro.nuclearindustry.tile.basicte;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.tile.IHasPorts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PortRenderer<T extends TileEntity & IHasPorts> extends FastTESR<T> {

    public static final ResourceLocation INPUT_PORT_TEX = new ResourceLocation(NuclearIndustry.MODID, "blocks/te/input_port");
    public static final ResourceLocation OUTPUT_PORT_TEX = new ResourceLocation(NuclearIndustry.MODID, "blocks/te/output_port");

    @Override
    public void renderTileEntityFast(T te, double x, double y, double z, float partialTicks, int destroyStage, float partial, BufferBuilder buffer) {
        buffer.setTranslation(x, y, z);

        for(EnumFacing facing : EnumFacing.VALUES){
            PortType type = te.getPortFromSide(facing);
            if(type == null || type == PortType.NONE_PORT) continue;

            int combinedLight = te.getWorld().getCombinedLight(te.getPos().offset(facing), 0);
            int lightMapU = combinedLight & 0xFFFF;
            int lightMapV = (combinedLight >> 16) & 0xFFFF;
            ResourceLocation texLoc = (type ==PortType.INPUT_PORT) ? INPUT_PORT_TEX : OUTPUT_PORT_TEX;
            TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texLoc.toString());
            renderFaceOverlay(buffer, facing, sprite, lightMapU, lightMapV);
        }
        buffer.setTranslation(0, 0, 0);
    }

    private void renderFaceOverlay(BufferBuilder buffer, EnumFacing facing, TextureAtlasSprite sprite, int skyLight, int blockLight) {
        float minU = sprite.getMinU();
        float maxU = sprite.getMaxU();
        float minV = sprite.getMinV();
        float maxV = sprite.getMaxV();

        float o = 0.001f; // maly offset zebu nie bylo z'fighting

        float factor;
        int color = 255;
        switch (facing) {
            case UP:    factor = 1.0f; break;
            case DOWN:  factor = 0.5f; break;
            case NORTH:
            case SOUTH: factor = 0.8f; break;
            case EAST:
            case WEST:  factor = 0.6f; break;
            default:    factor = 1.0f; break;
        }
        color = (int)(color * factor);

        switch (facing) {
            case DOWN:
                buffer.pos(0, -o, 1).color(color, color, color, 255).tex(minU, maxV).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(0, -o, 0).color(color, color, color, 255).tex(minU, minV).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(1, -o, 0).color(color, color, color, 255).tex(maxU, minV).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(1, -o, 1).color(color, color, color, 255).tex(maxU, maxV).lightmap(skyLight, blockLight).endVertex();
                break;
            case UP:
                buffer.pos(0, 1 + o, 0).color(color, color, color, 255).tex(minU, minV).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(0, 1 + o, 1).color(color, color, color, 255).tex(minU, maxV).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(1, 1 + o, 1).color(color, color, color, 255).tex(maxU, maxV).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(1, 1 + o, 0).color(color, color, color, 255).tex(maxU, minV).lightmap(skyLight, blockLight).endVertex();
                break;
            case NORTH:
                buffer.pos(1, 1, -o).color(color, color, color, 255).tex(minU, minV).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(1, 0, -o).color(color, color, color, 255).tex(minU, maxV).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(0, 0, -o).color(color, color, color, 255).tex(maxU, maxV).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(0, 1, -o).color(color, color, color, 255).tex(maxU, minV).lightmap(skyLight, blockLight).endVertex();
                break;
            case SOUTH:
                buffer.pos(0, 1, 1 + o).color(color, color, color, 255).tex(minU, minV).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(0, 0, 1 + o).color(color, color, color, 255).tex(minU, maxV).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(1, 0, 1 + o).color(color, color, color, 255).tex(maxU, maxV).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(1, 1, 1 + o).color(color, color, color, 255).tex(maxU, minV).lightmap(skyLight, blockLight).endVertex();
                break;
            case WEST:
                buffer.pos(-o, 1, 0).color(color, color, color, 255).tex(minU, minV).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(-o, 0, 0).color(color, color, color, 255).tex(minU, maxV).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(-o, 0, 1).color(color, color, color, 255).tex(maxU, maxV).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(-o, 1, 1).color(color, color, color, 255).tex(maxU, minV).lightmap(skyLight, blockLight).endVertex();
                break;
            case EAST:
                buffer.pos(1 + o, 1, 1).color(color, color, color, 255).tex(minU, minV).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(1 + o, 0, 1).color(color, color, color, 255).tex(minU, maxV).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(1 + o, 0, 0).color(color, color, color, 255).tex(maxU, maxV).lightmap(skyLight, blockLight).endVertex();
                buffer.pos(1 + o, 1, 0).color(color, color, color, 255).tex(maxU, minV).lightmap(skyLight, blockLight).endVertex();
                break;
        }
    }
}
