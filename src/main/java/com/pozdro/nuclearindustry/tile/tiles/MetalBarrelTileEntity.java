package com.pozdro.nuclearindustry.tile.tiles;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.recipe.BasicMachineRecipe;
import com.pozdro.nuclearindustry.tile.basicte.*;
import com.pozdro.nuclearindustry.tile.chestte.BasicChestEntity;
import com.pozdro.nuclearindustry.tile.chestte.BasicChestGuiHandler;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;

public class MetalBarrelTileEntity extends BasicChestEntity {
    ItemStackHandler inventory;

    //helper function for procedurally generating slots in gui
    private static List<TileSlot> createGridSlots(int columns, int rows, int startX, int startY) {
        List<TileSlot> slots = new ArrayList<>();
        int slotIndex = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int x = startX + (col * 18);
                int y = startY + (row * 18);

                slots.add(new TileSlot(slotIndex++, SlotType.INPUT_SLOT, EnumFacing.DOWN, x, y));
            }
        }
        return slots;
    }

    private static final List<TileSlot> guiSlots = createGridSlots(9, 6, 8, 18);

    //constructor
    public MetalBarrelTileEntity() {
        super(guiSlots.toArray().length, guiSlots, "metal_barrel", 3);
        inventory = getInventoryHandler();

    }

    //gui handler getter
    @Override
    public BasicChestGuiHandler<? extends BasicChestEntity> getGuiHandler() {
        return new BasicChestGuiHandler<>(
                guiSlots,
                new ResourceLocation(NuclearIndustry.MODID, "textures/gui/metal_barrel_gui.png"),
                MetalBarrelTileEntity.class,
                "Metal Barrel",
                176,
                222,
                56,
                3);
    }

    //recipe list getter
    @Override
    protected List<BasicMachineRecipe> getRecipeList() {
        return Collections.emptyList();
    }

}
