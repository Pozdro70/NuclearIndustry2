package com.pozdro.nuclearindustry.compat.jei;

import com.pozdro.nuclearindustry.recipe.BasicMachineRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BasicMachineRecipeWrapper extends BlankRecipeWrapper {

    private final BasicMachineRecipe recipe;

    public BasicMachineRecipeWrapper(BasicMachineRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<ItemStack> itemInputs=new ArrayList<>();

        recipe.inputs().forEach(itemIngredient -> itemInputs.add(itemIngredient.stack()));

        ingredients.setInputs(ItemStack.class, itemInputs);


        List<ItemStack> itemOutputs=new ArrayList<>();

        recipe.outputs().forEach(itemIngredient -> itemOutputs.add(itemIngredient.stack()));

        ingredients.setOutputs(ItemStack.class, itemOutputs);
    }
}
