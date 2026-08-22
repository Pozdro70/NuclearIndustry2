package com.pozdro.nuclearindustry.compat.jei;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.block.ModBlocks;
import com.pozdro.nuclearindustry.item.ModItems;
import com.pozdro.nuclearindustry.recipe.BasicMachineRecipe;
import com.pozdro.nuclearindustry.tile.basicte.BasicMachineContainer;
import com.pozdro.nuclearindustry.tile.tiles.GrinderTileEntity;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

@JEIPlugin
public class ModJEIPlugin implements IModPlugin {

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        registry.addRecipeCategories(
                new BasicMachineRecipeCategory(registry.getJeiHelpers().getGuiHelper(),"grinder",GrinderTileEntity.basicMachineGuiHandler)
        );
    }

    @Override
    public void register(IModRegistry registry) {
        List<BasicMachineRecipeWrapper> recipes = new ArrayList<>();

        for (BasicMachineRecipe recipe : GrinderTileEntity.RECIPES) {
            recipes.add(new BasicMachineRecipeWrapper(recipe,GrinderTileEntity.basicMachineGuiHandler));
        }

        System.out.println(
                "GRINDER JEI RECIPES: " + GrinderTileEntity.RECIPES.size()
        );

        registry.addRecipes(recipes, NuclearIndustry.MODID+".grinder");
        registry.addRecipeCatalyst(new ItemStack(ModBlocks.GRINDER),NuclearIndustry.MODID+".grinder");

        registry.getRecipeTransferRegistry().addRecipeTransferHandler(
                BasicMachineContainer.class,
                NuclearIndustry.MODID + ".grinder",
                0, //first machine slot
                8, //number of machine slots
                9,//first inventory slot
                36//number of inventory slots
        );
    }
}
