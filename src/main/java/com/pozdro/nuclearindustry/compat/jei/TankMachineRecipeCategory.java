package com.pozdro.nuclearindustry.compat.jei;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.recipe.FluidIngredient;
import com.pozdro.nuclearindustry.tile.tankte.TankMachineGuiHandler;
import com.pozdro.nuclearindustry.tile.tankte.TankTileEntity;
import com.pozdro.nuclearindustry.tile.tankte.TileTank;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;

public class TankMachineRecipeCategory implements IRecipeCategory<TankMachineRecipeWrapper> {

    private final String uid;
    private final TankMachineGuiHandler<? extends TankTileEntity> tankMachineGui;

    private final IDrawable background;
    private final IDrawableAnimated arrow;

    public TankMachineRecipeCategory(IGuiHelper helper, String uid, TankMachineGuiHandler<? extends TankTileEntity> basicMachineGui) {
        this.uid = NuclearIndustry.MODID  + "." + uid;
        this.tankMachineGui = basicMachineGui;

        background = helper.createDrawable(
                basicMachineGui.getGuiTexture(),
                4, 4,
                basicMachineGui.getGuiWitdh()-29, basicMachineGui.getGuiHeight()-90
        );

        arrow=helper.drawableBuilder(
                basicMachineGui.getGuiTexture(),
                basicMachineGui.getArrowSpriteX(),basicMachineGui.getArrowSpriteY(),
                basicMachineGui.getArrowWidthPx(),basicMachineGui.getArrowHightPx()
        ).buildAnimated(
                100,
                IDrawableAnimated.StartDirection.LEFT,
                false
        );


    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public String getTitle() {
        return tankMachineGui.getContainerName();
    }

    @Override
    public String getModName() {
        return NuclearIndustry.MODID;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        arrow.draw(minecraft,tankMachineGui.getArrowDrawX()-4,tankMachineGui.getArrowDrawY()-4);
    }

    @Override
    public void setRecipe(IRecipeLayout layout, TankMachineRecipeWrapper wrapper, IIngredients ingredients) {
        IGuiItemStackGroup items =
                layout.getItemStacks();

        List<ItemStack> itemInputs =
                ingredients.getInputs(VanillaTypes.ITEM).get(0);

        List<ItemStack> itemOutputs =
                ingredients.getOutputs(VanillaTypes.ITEM).get(0);

        for (int i = 0; i < itemInputs.size(); i++) {

            items.init(
                    i,
                    true,
                    10 + i * 20,
                    40
            );

            items.set(
                    i,
                    itemInputs.get(i)
            );
        }

        int outputStart =
                itemInputs.size();

        for (int i = 0; i < itemOutputs.size(); i++) {

            items.init(
                    outputStart + i,
                    false,
                    130 + i * 20,
                    40
            );

            items.set(
                    outputStart + i,
                    itemOutputs.get(i)
            );
        }

        //FLUIDS

        IGuiFluidStackGroup fluids =
                layout.getFluidStacks();

        List<List<net.minecraftforge.fluids.FluidStack>> fluidInputs =
                ingredients.getInputs(VanillaTypes.FLUID);

        List<List<net.minecraftforge.fluids.FluidStack>> fluidOutputs =
                ingredients.getOutputs(VanillaTypes.FLUID);



        for (int i = 0; i < fluidInputs.size(); i++) {

            FluidIngredient ingredient =
                    wrapper.getRecipe()
                            .fluidInputs()
                            .get(i);

            int tankID =
                    ingredient.tankID();

            int x = getTankX(tankID);
            int y = getTankY(tankID);

            fluids.init(
                    i,
                    true,
                    x,
                    y,
                    16,
                    50,
                    4000,
                    false,
                    null
            );

            fluids.set(
                    i,
                    fluidInputs.get(i)
            );
        }

        int fluidOutputIndex =
                fluidInputs.size();

        for (int i = 0; i < fluidOutputs.size(); i++) {

            FluidIngredient ingredient =
                    wrapper.getRecipe()
                            .fluidOutputs()
                            .get(i);

            int tankID =
                    ingredient.tankID();

            int x = getTankX(tankID);
            int y = getTankY(tankID);

            fluids.init(
                    fluidOutputIndex + i,
                    false,
                    x,
                    y,
                    16,
                    50,
                    4000,
                    false,
                    null
            );

            fluids.set(
                    fluidOutputIndex + i,
                    fluidOutputs.get(i)
            );
        }
    }

    private int getTankY(int tankID) {
        for (TileTank guiTank : tankMachineGui.getGuiTanks()) {
            if(guiTank.getTankID()==tankID){
                return guiTank.getTankY();
            }
        }
        return -1;
    };

    private int getTankX(int tankID) {
        for (TileTank guiTank : tankMachineGui.getGuiTanks()) {
            if(guiTank.getTankID()==tankID){
                return guiTank.getTankX();
            }
        }
        return -1;
    };

}
