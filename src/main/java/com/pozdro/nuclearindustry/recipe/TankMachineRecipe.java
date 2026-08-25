package com.pozdro.nuclearindustry.recipe;

import java.util.List;

public class TankMachineRecipe extends BasicMachineRecipe implements MachineRecipe{

    private final List<FluidIngredient> fluidInputs;
    private final List<FluidIngredient> fluidOutputs;

    public List<FluidIngredient> fluidInputs() {
        return fluidInputs;
    }
    public List<FluidIngredient> fluidOutputs() {
        return fluidOutputs;
    }


    public TankMachineRecipe(List<ItemIngredient> inputs, List<FluidIngredient> fluidInputs, List<ItemIngredient> outputs, List<FluidIngredient> fluidOutputs, double energyNeeded, int processingTime) {
        super(inputs, outputs, energyNeeded, processingTime);
        this.fluidInputs = fluidInputs;
        this.fluidOutputs = fluidOutputs;
    }
}