package com.pozdro.nuclearindustry.tile.tiles;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.recipe.BasicMachineRecipe;
import com.pozdro.nuclearindustry.recipe.FluidIngredient;
import com.pozdro.nuclearindustry.recipe.ItemIngredient;
import com.pozdro.nuclearindustry.recipe.TankMachineRecipe;
import com.pozdro.nuclearindustry.tile.basicte.*;
import ic2.api.energy.prefab.BasicSink;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemStackHandler;

import java.util.*;

public class item_barrelTileEntity extends BasicChestEntity implements ITickable {
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
    public item_barrelTileEntity() {
        super(guiSlots.toArray().length, guiSlots, "item_barrel", 3);
        inventory = getInventoryHandler();

    }

    //gui handler getter
    @Override
    public BasicChestGuiHandler<? extends BasicChestEntity> getGuiHandler() {
        return new BasicChestGuiHandler<>(
                guiSlots,
                new ResourceLocation(NuclearIndustry.MODID, "textures/gui/item_barrel_gui.png"),
                item_barrelTileEntity.class,
                "Item Barrel",
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

    //hmm
    @Override
    public void update() {

    }
}
