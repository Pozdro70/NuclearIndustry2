package com.pozdro.nuclearindustry.recipe;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.block.tile.LeacherTileEntity;
import ic2.api.item.IC2Items;
import ic2.api.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

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

    //public static final IRecipe TEST2 = newRecipe(new ShapelessRecipes())

    @SubscribeEvent
    public static void registerCraftingRecipes(RegistryEvent.Register<IRecipe> event) {
        event.getRegistry().registerAll(modRecipes.toArray(new IRecipe[0]));
    }

    public static void removeCraftingRecipesByOutput(List<ItemStack> removedRecipes){
        ForgeRegistry<IRecipe> registry = (ForgeRegistry<IRecipe>) ForgeRegistries.RECIPES;

        for (IRecipe recipe : new ArrayList<>(registry.getValuesCollection())) {
            ItemStack output = recipe.getRecipeOutput();


            if(removedRecipes.contains(output)){
                registry.remove(recipe.getRegistryName());
            }
        }
    }

    public static void removeRecipes(){ //FMLPostInit

        removeCraftingRecipesByOutput(Arrays.asList(
                IC2Items.getItem("nuclear","uranium") //Enriched Uranium Nuclear Fuel
        ));

        IC2RecipeRemover.removeBasicMachineRecipesByInput(Recipes.oreWashing,Arrays.asList(
                IC2Items.getItem("crushed","uranium") //Crushed Uranium
        ));

        IC2RecipeRemover.removeBasicMachineRecipesByInput(Recipes.centrifuge,Arrays.asList(
                IC2Items.getItem("crushed","uranium") //Crushed Uranium
        ));

        IC2RecipeRemover.removeCannerBottleRecipesByOutput(Arrays.asList(
                IC2Items.getItem("uranium_fuel_rod") //Uranium Fuel Rod
        ));


    }

    public static void addModRecipes() {

    }
}
