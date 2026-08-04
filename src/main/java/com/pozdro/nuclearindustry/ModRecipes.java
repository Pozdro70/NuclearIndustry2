package com.pozdro.nuclearindustry;

import com.pozdro.nuclearindustry.block.ModBlocks;
import com.pozdro.nuclearindustry.item.ModItems;
import ic2.api.item.IC2Items;
import ic2.api.recipe.ICannerBottleRecipeManager;
import ic2.api.recipe.MachineRecipe;
import ic2.api.recipe.Recipes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber(modid = NuclearIndustry.MODID)
public class ModRecipes {

    private static final List<IRecipe> modRecipes=new ArrayList<>();


    public static <T extends IRecipe> T newRecipe(T recipe){
        modRecipes.add(recipe);
        return recipe;
    }

    /*
    public static final IRecipe TEST_RECIPE = newRecipe(new ShapedOreRecipe(null,
            new ItemStack(ModItems.test),
            "XXX",
            "XYX",
            "XXX",
            'X', ModBlocks.test,
            'Y', Items.ARROW
            ).setRegistryName("test_recipe")
    );
     */

    @SubscribeEvent
    public static void registerCraftingRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().registerAll(modRecipes.toArray(new IRecipe[0]));
    }

    public static void removeRecipes(){ //FMLPostInit
        final List<Item> removedRecipes = new ArrayList<>();

        removedRecipes.add(IC2Items.getItem("nuclear","uranium").getItem()); //Enriched Uranium Nuclear Fuel
        removedRecipes.add(IC2Items.getItem("uranium_fuel_rod").getItem()); //Uranium Fuel Rod


        ForgeRegistry<IRecipe> registry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;

        for (IRecipe recipe : new ArrayList<>(registry.getValuesCollection())) {
            ItemStack output = recipe.getRecipeOutput();


            if(removedRecipes.contains(output.getItem())){
                registry.remove(recipe.getRegistryName());
            }
        }



        @SuppressWarnings("unchecked")
        List<MachineRecipe<ICannerBottleRecipeManager.Input, ItemStack>> recipes =
                (List<MachineRecipe<ICannerBottleRecipeManager.Input, ItemStack>>) Recipes.cannerBottle.getRecipes();

        Iterator<MachineRecipe<ICannerBottleRecipeManager.Input, ItemStack>> it = recipes.iterator();

        while (it.hasNext()){
            MachineRecipe<ICannerBottleRecipeManager.Input, ItemStack> recipe = it.next();
            ItemStack output= recipe.getOutput();

            if(removedRecipes.contains(output.getItem())){
                it.remove();
            }
        }







    }

}
