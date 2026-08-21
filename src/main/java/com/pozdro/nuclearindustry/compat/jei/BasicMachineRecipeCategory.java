package com.pozdro.nuclearindustry.compat.jei;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.recipe.BasicMachineRecipe;
import com.pozdro.nuclearindustry.tile.basicte.*;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class BasicMachineRecipeCategory implements IRecipeCategory<BasicMachineRecipeWrapper> {
    private final String uid;
    private final BasicMachineGuiHandler<? extends BasicTileEntity> basicMachineGui;

    private final IDrawable background;
    private final IDrawableAnimated arrow;

    public BasicMachineRecipeCategory(IGuiHelper helper, String uid, BasicMachineGuiHandler<? extends BasicTileEntity> basicMachineGui) {
        this.uid = NuclearIndustry.MODID  + "." + uid;
        this.basicMachineGui = basicMachineGui;

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
        return uid; //Must match the UID in ModTileEntities
    }

    @Override
    public String getTitle() {
        return basicMachineGui.getContainerName();
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
        arrow.draw(minecraft,basicMachineGui.getArrowDrawX()-4,basicMachineGui.getArrowDrawY()-4);
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, BasicMachineRecipeWrapper recipeWrapper, IIngredients ingredients) {

        for (TileSlot guiSlot : basicMachineGui.getGuiSlots()) {
            recipeLayout.getItemStacks().init(guiSlot.getSlotID(),guiSlot.getSlotType()== SlotType.INPUT_SLOT,
                    guiSlot.getSlotXCord()-5,guiSlot.getSlotYCord()-5);
        }

        recipeLayout.getItemStacks().set(ingredients);
    }
}
