package com.pozdro.nuclearindustry.block.tile;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BasicMachineGui<T extends TileEntity & IHasInventory & IHasProgressAndEnergy & ISettableTank> extends GuiContainer {

    private final ResourceLocation guiTexture;
    private final T tile;
    private final String containerName;
    private final boolean drawArrowHorizontally;
    private final int arrowHightPx, arrowWidthPx;
    private final int arrowDrawX, arrowDrawY;
    private final int arrowSpriteX, arrowSpriteY;
    private final int playerInvYOffset;
    private final int energyBarPosX,energyBarPosY;
    private final int energyBarRenderHeight;



    private final Map<Integer, Map.Entry<Integer, Integer>> tankSize;      // <tankID, <height, width>>
    private final Map<Integer, Map.Entry<Integer, Integer>> tankPos;       // <tankID, <x, y>>


    public BasicMachineGui(InventoryPlayer playerInv, T tile, Map<Integer, Map.Entry<Integer, Integer>> guiSlots,
                           ResourceLocation guiTexture, String containerName, int guiWidth, int guiHeight,
                           boolean drawArrowHorizontally, int arrowHightPx, int arrowWidthPx, int arrowDrawX,
                           int arrowDrawY, int arrowSpriteX, int arrowSpriteY, int playerInvYOffset,
                           Map<Integer, Map.Entry<Integer, Integer>> tankSize,
                           Map<Integer, Map.Entry<Integer, Integer>> tankPos, int energyBarPosX, int energyBarPosY, int energyBarHeight) {
        super(new BasicMachineContainer<T>(playerInv, tile, guiSlots,playerInvYOffset));
        this.tile = tile;
        this.guiTexture = guiTexture;
        this.containerName = containerName;
        this.drawArrowHorizontally = drawArrowHorizontally;
        this.arrowHightPx = arrowHightPx;
        this.arrowWidthPx = arrowWidthPx;
        this.arrowDrawX = arrowDrawX;
        this.arrowDrawY = arrowDrawY;
        this.arrowSpriteX = arrowSpriteX;
        this.arrowSpriteY = arrowSpriteY;
        this.playerInvYOffset = playerInvYOffset;
        this.tankSize = tankSize;
        this.tankPos = tankPos;
        this.energyBarPosX = energyBarPosX;
        this.energyBarPosY = energyBarPosY;
        this.energyBarRenderHeight = energyBarHeight;


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
            drawTexturedModalRect(guiLeft + arrowDrawX, guiTop + arrowDrawY,
                    arrowSpriteX, arrowSpriteY, arrowFill, arrowHightPx);
        } else {
            int fillOffset = arrowWidthPx - arrowFill; // grow upward from the bottom
            drawTexturedModalRect(guiLeft + arrowDrawX, guiTop + arrowDrawY + fillOffset,
                    arrowSpriteX, arrowSpriteY + fillOffset, arrowHightPx, arrowFill);
        }


        // ---- fluid tanks ----
        tile.getFluidTanks().forEach((tankID, fluidTank) -> {
            FluidStack fluid = fluidTank.getFluid();
            if (fluid == null || fluid.amount <= 0) return;

            Map.Entry<Integer, Integer> size = tankSize.get(tankID); // <height, width>
            Map.Entry<Integer, Integer> pos = tankPos.get(tankID);   // <x, y>
            int tankHeightPx = size.getKey();
            int tankWidthPx = size.getValue();

            long capacity = fluidTank.getCapacity();
            int fillHeight = (int) ((long) tankHeightPx * fluid.amount / capacity);
            if (fillHeight <= 0) return;

            FluidRenderHelper.drawFluid(
                    guiLeft + pos.getKey(),
                    guiTop + pos.getValue() + (tankHeightPx - fillHeight),
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


        tile.getFluidTanks().forEach((tankID, fluidTank) -> {
            Map.Entry<Integer, Integer> size = tankSize.get(tankID);
            Map.Entry<Integer, Integer> pos = tankPos.get(tankID);
            int left = guiLeft + pos.getKey();
            int top = guiTop + pos.getValue();
            int right = left + size.getValue();
            int bottom = top + size.getKey();

            if (mouseX >= left && mouseX <= right && mouseY >= top && mouseY <= bottom) {
                List<String> tooltip = new ArrayList<>();
                FluidStack fluid = fluidTank.getFluid();
                if (fluid != null) {
                    tooltip.add(fluid.getLocalizedName() + ": " + fluid.amount + " / " + fluidTank.getCapacity() + " mB");
                } else {
                    tooltip.add("Empty: 0 / " + fluidTank.getCapacity() + " mB");
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