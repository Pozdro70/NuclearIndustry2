package com.pozdro.nuclearindustry.recipe;

import java.util.List;

public class BasicMachineRecipe {

    private final List<ItemIngredient> inputs;
    private final List<ItemIngredient> outputs;
    private final double energyNeeded;
    private final int processingTime;

    public BasicMachineRecipe(
            List<ItemIngredient> inputs,
            List<FluidIngredient> fluidInputs,
            List<ItemIngredient> outputs,
            List<FluidIngredient> fluidOutputs,
            double energyNeeded,
            int processingTime) {

        this.inputs = inputs;
        this.outputs = outputs;
        this.energyNeeded = energyNeeded;
        this.processingTime = processingTime;
    }

    public List<ItemIngredient> inputs() {
        return inputs;
    }

    public List<ItemIngredient> outputs() {
        return outputs;
    }

    public double energyNeeded() {
        return energyNeeded;
    }

    public int processingTime() {
        return processingTime;
    }
}