package com.pozdro.nuclearindustry.compat.jei;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.block.ModBlocks;
import com.pozdro.nuclearindustry.recipe.BasicMachineRecipe;
import com.pozdro.nuclearindustry.tile.basicte.BasicMachineContainer;
import com.pozdro.nuclearindustry.tile.tiles.GrinderTileEntity;
import com.pozdro.nuclearindustry.tile.tiles.LeacherTileEntity;
import com.pozdro.nuclearindustry.tile.tiles.PurifierPressTileEntity;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JEIPlugin
public class ModJEIPlugin implements IModPlugin {

    private static final List<? extends JEIMachineDefinition> MACHINES = Arrays.asList(
            new JEIMachineDefinition(
                    "grinder",
                    GrinderTileEntity.RECIPES,
                    GrinderTileEntity.basicMachineGuiHandler,
                    ModBlocks.GRINDER
            ),
            new JEIMachineDefinition(
                    "leacher",
                    LeacherTileEntity.RECIPES,
                    LeacherTileEntity.tankMachineGuiHandler,
                    ModBlocks.LEACHER
            ),
            new JEIMachineDefinition(
                    "purifier_press",
                    PurifierPressTileEntity.RECIPES,
                    PurifierPressTileEntity.tankMachineGuiHandler,
                    ModBlocks.LEACHER
            )
    );


    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        List<IRecipeCategory<?>> machinesRegistry=new ArrayList<>();

        for (JEIMachineDefinition machine : MACHINES) {
            machinesRegistry.add(new BasicMachineRecipeCategory(registry.getJeiHelpers().getGuiHelper(),machine.getId(),machine.getGuiHandler()));
        }

        registry.addRecipeCategories(machinesRegistry.toArray(new IRecipeCategory[0]));
    }


    private void registerMachine(
            IModRegistry registry,
            JEIMachineDefinition machine
    ) {
        String recipeId = NuclearIndustry.MODID + "." + machine.getId();

        List<BasicMachineRecipeWrapper> wrappers = new ArrayList<>();

        for (BasicMachineRecipe recipe : machine.getRecipes()) {
            wrappers.add(
                    new BasicMachineRecipeWrapper(
                            recipe,
                            machine.getGuiHandler()
                    )
            );
        }

        registry.addRecipes(wrappers, recipeId);

        registry.addRecipeCatalyst(
                new ItemStack(machine.getMachineBlock()),
                recipeId
        );

        registry.getRecipeTransferRegistry().addRecipeTransferHandler(
                BasicMachineContainer.class,
                recipeId,
                machine.getMachineSlot(),
                machine.getMachineSlotCount(),
                machine.getInventorySlot(),
                machine.getInventorySlotCount()
        );
    }


    @Override
    public void register(IModRegistry registry) {
        for (JEIMachineDefinition machine : MACHINES) {
            registerMachine(registry, machine);
        }
    }
}
