package com.pozdro.nuclearindustry.tile.tankte;

import com.pozdro.nuclearindustry.tile.IHasInventory;
import com.pozdro.nuclearindustry.tile.IHasProgressAndEnergy;
import com.pozdro.nuclearindustry.tile.ISettableTank;
import com.pozdro.nuclearindustry.tile.basicte.TileSlot;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TankMachineGui<T extends TileEntity & IHasInventory & IHasProgressAndEnergy & ISettableTank> extends GuiContainer {

    private final ResourceLocation guiTexture;
    private final T tile;
    private final String containerName;
    private final boolean drawArrowHorizontally,invertDrawDirection;
    private final int arrowHightPx, arrowWidthPx;
    private final int arrowDrawX, arrowDrawY;
    private final int arrowSpriteX, arrowSpriteY;
    private final int playerInvYOffset;
    private final int energyBarPosX,energyBarPosY;
    private final int energyBarRenderHeight;
    List<TileTank> tanks;


    public TankMachineGui(InventoryPlayer playerInv, T tile, List<TileSlot> guiSlots,
                          ResourceLocation guiTexture, String containerName, int guiWidth, int guiHeight,
                          boolean drawArrowHorizontally, boolean invertDrawDirection, int arrowHightPx, int arrowWidthPx, int arrowDrawX,
                          int arrowDrawY, int arrowSpriteX, int arrowSpriteY, int playerInvYOffset,
                          int energyBarPosX, int energyBarPosY, int energyBarHeight, List<TileTank> tanks) {

        super(new TankMachineContainer<T>(playerInv, tile, guiSlots,playerInvYOffset));
        this.tile = tile;
        this.guiTexture = guiTexture;
        this.containerName = containerName;
        this.drawArrowHorizontally = drawArrowHorizontally;
        this.invertDrawDirection = invertDrawDirection;
        this.arrowHightPx = arrowHightPx;
        this.arrowWidthPx = arrowWidthPx;
        this.arrowDrawX = arrowDrawX;
        this.arrowDrawY = arrowDrawY;
        this.arrowSpriteX = arrowSpriteX;
        this.arrowSpriteY = arrowSpriteY;
        this.playerInvYOffset = playerInvYOffset;

        this.energyBarPosX = energyBarPosX;
        this.energyBarPosY = energyBarPosY;
        this.energyBarRenderHeight = energyBarHeight;
        this.tanks=tanks;

        this.xSize = guiWidth;
        this.ySize = guiHeight;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(guiTexture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        // ---- progress arrow ----
        int arrowFill = tile.getMaxProgress() == 0 ? 0
                : tile.getProgress() * arrowWidthPx / tile.getMaxProgress();
        if (drawArrowHorizontally) {
            int fillOffset = 0;

            if (invertDrawDirection)
                fillOffset = arrowWidthPx - arrowFill;

            drawTexturedModalRect(
                    guiLeft + arrowDrawX + fillOffset,
                    guiTop + arrowDrawY,
                    arrowSpriteX + fillOffset,
                    arrowSpriteY,
                    arrowFill,
                    arrowHightPx
            );

        } else {
            int fillOffset = 0;

            if (invertDrawDirection)
                fillOffset = arrowHightPx - arrowFill;

            drawTexturedModalRect(
                    guiLeft + arrowDrawX,
                    guiTop + arrowDrawY + fillOffset,
                    arrowSpriteX,
                    arrowSpriteY + fillOffset,
                    arrowWidthPx,
                    arrowFill
            );
        }


        // ---- fluid tanks ----
        tile.getFluidTanks().forEach((tileTank) -> {
            FluidStack fluid = tileTank.getTank().getFluid();
            if (fluid == null || fluid.amount <= 0) return;

            int tankHeightPx = tileTank.getTankYSize();
            int tankWidthPx = tileTank.getTankXSize();

            long capacity = tileTank.getTank().getCapacity();
            int fillHeight = (int) ((long) tankHeightPx * fluid.amount / capacity);
            if (fillHeight <= 0) return;

            FluidRenderHelper.drawFluid(
                    guiLeft + tileTank.getTankX(),
                    guiTop + tileTank.getTankY() + (tankHeightPx - fillHeight),
                    tankWidthPx, fillHeight, fluid
            );

        });


        // ---- energy bar ----
        int energyBarHeight= tile.getClientEnergy()*energyBarRenderHeight/ (int) tile.getEnergySink().getCapacity();

        //back gradient
        drawRect(guiLeft + energyBarPosX - 1,
                guiTop + energyBarPosY - 1,
                guiLeft + energyBarPosX + 7,
                guiTop + energyBarPosY + energyBarRenderHeight + 1,
                0xFF333333);

        drawGradientRect(guiLeft + energyBarPosX,
                guiTop + energyBarPosY,
                guiLeft + energyBarPosX + 6,
                guiTop + energyBarPosY + energyBarRenderHeight,
                0xFF444444,
                0xFF3A3A3A);

        //energy bar
        drawGradientRect(
                guiLeft+energyBarPosX,
                guiTop+energyBarPosY + (energyBarRenderHeight-energyBarHeight),
                guiLeft+energyBarPosX+6, //6px wide
                guiTop+energyBarPosY+energyBarRenderHeight,
                0xFFFF4444, //Top: bright red
                0xFF880000  //Bottom: dark red
        );
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(I18n.format(containerName), 8, 6, 0x404040);
        fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 0x404040);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX,mouseY);
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        super.renderHoveredToolTip(mouseX, mouseY);


        tile.getFluidTanks().forEach((tileTank) -> {
            int left = guiLeft + tileTank.getTankX();
            int top = guiTop + tileTank.getTankY();

            int right = left + tileTank.getTankXSize();
            int bottom = top + tileTank.getTankYSize();

            if (mouseX >= left && mouseX <= right && mouseY >= top && mouseY <= bottom) {
                List<String> tooltip = new ArrayList<>();
                FluidStack fluid = tileTank.getTank().getFluid();
                if (fluid != null) {
                    tooltip.add(fluid.getLocalizedName() + ": " + fluid.amount + " / " + tileTank.getTank().getCapacity() + " mB");
                } else {
                    tooltip.add("Empty: 0 / " + tileTank.getTank().getCapacity() + " mB");
                }
                drawHoveringText(tooltip, mouseX, mouseY);
            }
        });


        int x = guiLeft + energyBarPosX;
        int y = guiTop + energyBarPosY;

        if (mouseX >= x && mouseX <= x + 6 &&
                mouseY >= y && mouseY <= y + energyBarRenderHeight) {

            List<String> tooltip = new ArrayList<>();

            int energy = tile.getClientEnergy();
            int maxEnergy = 10000;

            tooltip.add(energy + " / " + maxEnergy + " EU");
            tooltip.add((energy * 100 / maxEnergy) + "%");

            drawHoveringText(tooltip, mouseX, mouseY);
            return;
        }

        super.renderHoveredToolTip(mouseX, mouseY);

    }

}