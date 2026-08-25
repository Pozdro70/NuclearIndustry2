package com.pozdro.nuclearindustry.compat.jei;

import com.pozdro.nuclearindustry.recipe.FluidIngredient;
import com.pozdro.nuclearindustry.recipe.ItemIngredient;
import com.pozdro.nuclearindustry.recipe.TankMachineRecipe;
import com.pozdro.nuclearindustry.tile.basicte.TileSlot;
import com.pozdro.nuclearindustry.tile.tankte.TankMachineGuiHandler;
import com.pozdro.nuclearindustry.tile.tankte.TankTileEntity;
import com.pozdro.nuclearindustry.tile.tankte.TileTank;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class TankMachineRecipeWrapper implements IRecipeWrapper {
    private final TankMachineRecipe recipe;
    private final TankMachineGuiHandler<? extends TankTileEntity> guiHandler;

    public TankMachineRecipeWrapper(TankMachineRecipe recipe, TankMachineGuiHandler<? extends TankTileEntity> guiHandler) {
        this.recipe = recipe;
        this.guiHandler = guiHandler;
    }

    public TankMachineRecipe getRecipe() {
        return recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        //ITEMS
        List<ItemStack> itemInputs=new ArrayList<>();

        for (ItemIngredient input : recipe.inputs()) {
            itemInputs.add(input.stack());
        }

        ingredients.setInputs(VanillaTypes.ITEM,itemInputs);

        List<ItemStack> itemOutputs = new ArrayList<>();

        for (ItemIngredient ingredient : recipe.outputs()) {
            itemOutputs.add(ingredient.stack());
        }

        ingredients.setOutputs(
                VanillaTypes.ITEM,
                itemOutputs
        );


        // FLUIDS
        List<FluidStack> fluidInputs = new ArrayList<>();

        for (FluidIngredient ingredient : recipe.fluidInputs()) {
            fluidInputs.add(ingredient.stack());
        }

        ingredients.setInputs(
                VanillaTypes.FLUID,
                fluidInputs
        );

        List<FluidStack> fluidOutputs = new ArrayList<>();

        for (FluidIngredient ingredient : recipe.fluidOutputs()) {
            fluidOutputs.add(ingredient.stack());
        }

        ingredients.setOutputs(
                VanillaTypes.FLUID,
                fluidOutputs
        );

    }

    @Override
    public void drawInfo(
            Minecraft minecraft,
            int recipeWidth,
            int recipeHeight,
            int mouseX,
            int mouseY) {

        for (ItemIngredient ingredient : recipe.outputs()) {

            TileSlot guiSlot = findSlot(ingredient.slot());

            if (guiSlot == null) {
                continue;
            }

            if (ingredient.probability() == 100 ||
                    ingredient.probability() == 0) {
                continue;
            }

            drawProbability(
                    minecraft,
                    ingredient.probability(),
                    guiSlot.getSlotXCord(),
                    guiSlot.getSlotYCord()
            );
        }

        for (FluidIngredient ingredient : recipe.fluidOutputs()) {

            TileTank guiTank = findTank(ingredient.tankID());

            if(guiTank==null){
                continue;
            }

            if (ingredient.probability() == 100 ||
                    ingredient.probability() == 0) {
                continue;
            }

            drawProbability(
                    minecraft,
                    ingredient.probability(),
                    guiTank.getTankX(),
                    guiTank.getTankY()
            );
        }
    }

    private void drawProbability(
            Minecraft minecraft,
            int probability,
            int x,
            int y) {

        String text = probability + "%";

        int textWidth =
                minecraft.fontRenderer.getStringWidth(text);

        minecraft.fontRenderer.drawString(
                text,
                x - textWidth / 2 + 5,
                y + 18,
                0x555555
        );
    }

    private TileSlot findSlot(int slotID) {

        for (TileSlot slot : guiHandler.getGuiSlots()) {

            if (slot.getSlotID() == slotID) {
                return slot;
            }
        }

        return null;
    }

    private TileTank findTank(int tankID) {

        for (TileTank tank : guiHandler.getGuiTanks()) {

            if (tank.getTankID() == tankID) {
                return tank;
            }
        }

        return null;
    }
}
