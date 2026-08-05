package com.pozdro.nuclearindustry.recipe;

import ic2.api.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class IC2RecipeRemover {
    //I DON'T RECOMMEND USING THIS SHITTY CODE FROM NOW ON, but if you do, please credit me somehow, I spent 5 hours on this shit
    //if someone from ic2 devteam reads this, PLEASE add something like Recipes.whateverMachine.remove(recipe)

    @SuppressWarnings("unchecked")
    public static void removeBasicMachineRecipesByInput(
            IBasicMachineRecipeManager manager,
            List<ItemStack> stacksToRemove
    ) {
        /*
        SHOULD work for those:
        Recipes.
            macerator
            extractor
            compressor
            centrifuge
            blockcutter
            blastfurnace
            recycler
            metalformerExtruding
            metalformerCutting
            metalformerRolling
            oreWashing

        for
        + Recipes.cannerBottle use removeCannerBottleRecipesByOutput(...)

        and finally there is no function to remove recipes for those because I am too lazy to write it, maybe in the next update
        - Recipes.scrapbox
        - Recipes.semiFluidGenerator

        Example usage is in ModRecipes (nuclearindustry.recipe.ModRecipes)

        */

        @SuppressWarnings("unchecked")
        Iterable<MachineRecipe<IRecipeInput, Collection<ItemStack>>> recipes =
                (Iterable<MachineRecipe<IRecipeInput, Collection<ItemStack>>>) manager.getRecipes();

        Iterator<MachineRecipe<IRecipeInput, Collection<ItemStack>>> it = recipes.iterator();

        while (it.hasNext()) {
            MachineRecipe<IRecipeInput, Collection<ItemStack>> recipe = it.next();

            boolean remove = false;

            for (ItemStack input : recipe.getInput().getInputs()) {

                for (ItemStack target : stacksToRemove) {

                    if (input.isItemEqual(target)) {
                        remove = true;
                        break;
                    }
                }

                if (remove) {
                    break;
                }
            }

            if (remove) {
                it.remove();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void removeCannerBottleRecipesByOutput(List<ItemStack> removedRecipes){
        List<MachineRecipe<ICannerBottleRecipeManager.Input, ItemStack>> cannerBottleRecipes =
                (List<MachineRecipe<ICannerBottleRecipeManager.Input, ItemStack>>) Recipes.cannerBottle.getRecipes();

        Iterator<MachineRecipe<ICannerBottleRecipeManager.Input, ItemStack>> cannerBottleIt = cannerBottleRecipes.iterator();

        while (cannerBottleIt.hasNext()){
            MachineRecipe<ICannerBottleRecipeManager.Input, ItemStack> recipe = cannerBottleIt.next();
            ItemStack output= recipe.getOutput();

            if(removedRecipes.contains(output)){
                cannerBottleIt.remove();
            }
        }

    }
}
