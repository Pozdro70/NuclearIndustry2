package com.pozdro.nuclearindustry.compat.jei;

import com.pozdro.nuclearindustry.recipe.BasicMachineRecipe;
import com.pozdro.nuclearindustry.recipe.ItemIngredient;
import com.pozdro.nuclearindustry.tile.basicte.BasicMachineGuiHandler;
import com.pozdro.nuclearindustry.tile.basicte.BasicTileEntity;
import com.pozdro.nuclearindustry.tile.basicte.TileSlot;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BasicMachineRecipeWrapper implements IRecipeWrapper {

    private final BasicMachineRecipe recipe;
    private final BasicMachineGuiHandler<? extends BasicTileEntity> guiHandler;

    public BasicMachineRecipeWrapper(
            BasicMachineRecipe recipe,
            BasicMachineGuiHandler<? extends BasicTileEntity> guiHandler) {

        this.recipe = recipe;
        this.guiHandler = guiHandler;
    }

    public BasicMachineRecipe getRecipe() {
        return recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {

        List<ItemStack> inputs = new ArrayList<>();

        for (ItemIngredient ingredient : recipe.inputs()) {
            inputs.add(ingredient.stack());
        }

        ingredients.setInputs(
                VanillaTypes.ITEM,
                inputs
        );

        List<ItemStack> outputs = new ArrayList<>();

        for (ItemIngredient ingredient : recipe.outputs()) {
            outputs.add(ingredient.stack());
        }

        ingredients.setOutputs(
                VanillaTypes.ITEM,
                outputs
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

            if(ingredient.probability()==100 || ingredient.probability()==0){continue;}

            String text = ingredient.probability() + "%";

            int textWidth =
                    minecraft.fontRenderer.getStringWidth(text);

            int x = guiSlot.getSlotXCord()
                    - textWidth / 2+5;

            int y = guiSlot.getSlotYCord() + 18;

            minecraft.fontRenderer.drawString(
                    text,
                    x,
                    y,
                    0x555555
            );
        }
    }

    private TileSlot findSlot(int slotID) {

        for (TileSlot slot : guiHandler.getGuiSlots()) {

            if (slot.getSlotID() == slotID) {
                return slot;
            }
        }

        return null;
    }
}
