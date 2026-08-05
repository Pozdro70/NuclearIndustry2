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
            List<Item> itemsToRemove
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

        try {
            Class<?> helperClass = Class.forName("ic2.core.recipe.MachineRecipeHelper");

            Field recipesField = helperClass.getDeclaredField("recipes");
            recipesField.setAccessible(true);

            Map<IRecipeInput, MachineRecipe<IRecipeInput, Collection<ItemStack>>> recipes =
                    (Map<IRecipeInput, MachineRecipe<IRecipeInput, Collection<ItemStack>>>)
                            recipesField.get(manager);

            Method removeCachedRecipes =
                    helperClass.getDeclaredMethod("removeCachedRecipes", Object.class);
            removeCachedRecipes.setAccessible(true);

            Iterator<Map.Entry<IRecipeInput, MachineRecipe<IRecipeInput, Collection<ItemStack>>>> it =
                    recipes.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<IRecipeInput, MachineRecipe<IRecipeInput, Collection<ItemStack>>> entry = it.next();

                boolean remove = false;

                for (ItemStack input : entry.getKey().getInputs()) {
                    if (itemsToRemove.contains(input.getItem())) {
                        remove = true;
                        break;
                    }
                }

                if (remove) {
                    IRecipeInput recipeInput = entry.getKey();

                    it.remove();
                    removeCachedRecipes.invoke(manager, recipeInput);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("---IC2RecipeRemoval Error End---");
        }
    }

    @SuppressWarnings("unchecked")
    public static void removeCannerBottleRecipesByOutput(List<Item> removedRecipes){
        List<MachineRecipe<ICannerBottleRecipeManager.Input, ItemStack>> cannerBottleRecipes =
                (List<MachineRecipe<ICannerBottleRecipeManager.Input, ItemStack>>) Recipes.cannerBottle.getRecipes();

        Iterator<MachineRecipe<ICannerBottleRecipeManager.Input, ItemStack>> cannerBottleIt = cannerBottleRecipes.iterator();

        while (cannerBottleIt.hasNext()){
            MachineRecipe<ICannerBottleRecipeManager.Input, ItemStack> recipe = cannerBottleIt.next();
            ItemStack output= recipe.getOutput();

            if(removedRecipes.contains(output.getItem())){
                cannerBottleIt.remove();
            }
        }

    }
}
