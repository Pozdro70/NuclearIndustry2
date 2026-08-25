package com.pozdro.nuclearindustry.compat.jei;

import com.pozdro.nuclearindustry.recipe.BasicMachineRecipe;
import com.pozdro.nuclearindustry.tile.basicte.BasicMachineGuiHandler;
import com.pozdro.nuclearindustry.tile.basicte.BasicTileEntity;
import net.minecraft.block.Block;

import java.util.List;

public class JEIMachineDefinition {
    private final String id;
    private final List<? extends BasicMachineRecipe> recipes;
    private final BasicMachineGuiHandler<? extends BasicTileEntity> guiHandler;
    private final Block machineBlock;
    private final int machineSlot;
    private final int machineSlotCount;
    private final int inventorySlot;
    private final int inventorySlotCount;

    public JEIMachineDefinition(
            String id,
            List<? extends BasicMachineRecipe> recipes,
            BasicMachineGuiHandler<? extends BasicTileEntity> guiHandler,
            Block machineItem
    ) {
        this.id = id;
        this.recipes = recipes;
        this.guiHandler = guiHandler;
        this.machineBlock = machineItem;
        this.machineSlot = 0;
        this.machineSlotCount = guiHandler.getGuiSlots().size();
        this.inventorySlot = guiHandler.getGuiSlots().size()+1;
        this.inventorySlotCount = 36;
    }

    public int getInventorySlotCount() {
        return inventorySlotCount;
    }

    public int getInventorySlot() {
        return inventorySlot;
    }

    public int getMachineSlotCount() {
        return machineSlotCount;
    }

    public int getMachineSlot() {
        return machineSlot;
    }

    public Block getMachineBlock() {
        return machineBlock;
    }

    public BasicMachineGuiHandler<? extends BasicTileEntity> getGuiHandler() {
        return guiHandler;
    }

    public List<? extends BasicMachineRecipe> getRecipes() {
        return recipes;
    }

    public String getId() {
        return id;
    }
}
