package com.pozdro.nuclearindustry.recipe;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.fluid.ModFluids;
import com.pozdro.nuclearindustry.item.ModItems;
import com.pozdro.nuclearindustry.tile.tiles.GrinderTileEntity;
import com.pozdro.nuclearindustry.tile.tiles.LeacherTileEntity;
import com.pozdro.nuclearindustry.tile.tiles.PurifierPressTileEntity;
import ic2.api.item.IC2Items;
import ic2.api.recipe.*;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

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
        LeacherTileEntity.addRecipe(new TankMachineRecipe(
                Arrays.asList(
                        new ItemIngredient(2, new ItemStack(ModItems.COPPEROREDUST, 2), 100)
                ),
                Arrays.asList(
                        new FluidIngredient(0, new FluidStack(ModFluids.SULFURIC_ACID, 500), 100)
                ),
                Arrays.asList(
                        new ItemIngredient(4,new ItemStack(IC2Items.getItem("dust","stone").getItem(),1),70)
                ),
                Arrays.asList(
                        new FluidIngredient(1, new FluidStack(ModFluids.COPPER_LEACHATE, 800), 100)
                ),
                700,
                400
        ));

        PurifierPressTileEntity.addRecipe(new TankMachineRecipe(
                null,
                Arrays.asList(
                        new FluidIngredient(0, new FluidStack(ModFluids.COPPER_LEACHATE, 1000), 100)
                ),
                Arrays.asList(
                        new ItemIngredient(2,new ItemStack(IC2Items.getItem("dust","stone").getItem(),1),70)
                ),
                Arrays.asList(
                        new FluidIngredient(1, new FluidStack(ModFluids.COPPER_SOLUTION, 800), 100)
                ),
                700,
                500
        ));


        GrinderTileEntity.addRecipe(new BasicMachineRecipe(
                Arrays.asList(
                        new ItemIngredient(0,new ItemStack(IC2Items.getItem("crushed","copper").getItem(),1),100)
                ),
                Arrays.asList(
                        new ItemIngredient(3,new ItemStack(ModItems.COPPEROREDUST,1),100),
                        new ItemIngredient(4,new ItemStack(ModItems.COPPEROREDUST,1),50)
                ),
                600,
                100
        ));

        GrinderTileEntity.addRecipe(new BasicMachineRecipe(
                Arrays.asList(
                        new ItemIngredient(0,new ItemStack(IC2Items.getItem("crushed","gold").getItem(),1),100)
                ),
                Arrays.asList(
                        new ItemIngredient(3,new ItemStack(ModItems.GOLDOREDUST,1),100),
                        new ItemIngredient(3,new ItemStack(ModItems.GOLDOREDUST,1),50)
                ),
                600,
                100
        ));

        GrinderTileEntity.addRecipe(new BasicMachineRecipe(
                Arrays.asList(
                        new ItemIngredient(0,new ItemStack(IC2Items.getItem("crushed","iron").getItem(),1),100)
                ),
                Arrays.asList(
                        new ItemIngredient(3,new ItemStack(ModItems.IRONOREDUST,1),100),
                        new ItemIngredient(3,new ItemStack(ModItems.IRONOREDUST,1),50)
                ),
                600,
                100
        ));

        GrinderTileEntity.addRecipe(new BasicMachineRecipe(
                Arrays.asList(
                        new ItemIngredient(0,new ItemStack(IC2Items.getItem("crushed","lead").getItem(),1),100)
                ),
                Arrays.asList(
                        new ItemIngredient(3,new ItemStack(ModItems.LEADOREDUST,1),100),
                        new ItemIngredient(3,new ItemStack(ModItems.LEADOREDUST,1),50)
                ),
                600,
                100
        ));

        GrinderTileEntity.addRecipe(new BasicMachineRecipe(
                Arrays.asList(
                        new ItemIngredient(0,new ItemStack(IC2Items.getItem("crushed","silver").getItem(),1),100)
                ),
                Arrays.asList(
                        new ItemIngredient(3,new ItemStack(ModItems.SILVEROREDUST,1),100),
                        new ItemIngredient(3,new ItemStack(ModItems.SILVEROREDUST,1),50)
                ),
                600,
                100
        ));

        GrinderTileEntity.addRecipe(new BasicMachineRecipe(
                Arrays.asList(
                        new ItemIngredient(0,new ItemStack(IC2Items.getItem("crushed","tin").getItem(),1),100)
                ),
                Arrays.asList(
                        new ItemIngredient(3,new ItemStack(ModItems.TINOREDUST,1),100),
                        new ItemIngredient(3,new ItemStack(ModItems.TINOREDUST,1),50)
                ),
                600,
                100
        ));

        GrinderTileEntity.addRecipe(new BasicMachineRecipe(
                Arrays.asList(
                        new ItemIngredient(0,new ItemStack(IC2Items.getItem("crushed","uranium").getItem(),1),100)
                ),
                Arrays.asList(
                        new ItemIngredient(3,new ItemStack(ModItems.URANIUMOREDUST,1),100),
                        new ItemIngredient(3,new ItemStack(ModItems.URANIUMOREDUST,1),50)
                ),
                600,
                100
        ));
    }
}
