package com.pozdro.nuclearindustry.tile.basicte;

import com.pozdro.nuclearindustry.tile.IHasInventory;
import com.pozdro.nuclearindustry.tile.IHasProgressAndEnergy;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class BasicChestGui<T extends TileEntity & IHasInventory> extends GuiContainer {

    private final ResourceLocation guiTexture;
    private final T tile;
    private final String containerName;
    private final int playerInvYOffset;
    


    public BasicChestGui(InventoryPlayer playerInv, T tile, List<TileSlot> guiSlots,
                         ResourceLocation guiTexture, String containerName, int guiWidth, int guiHeight,
                         int playerInvYOffset) {
        super(new BasicChestContainer<T>(playerInv, tile, guiSlots,playerInvYOffset));
        this.tile = tile;
        this.guiTexture = guiTexture;
        this.containerName = containerName;
        this.playerInvYOffset = playerInvYOffset;

        this.xSize = guiWidth;
        this.ySize = guiHeight;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        mc.getTextureManager().bindTexture(guiTexture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
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
    }
}
