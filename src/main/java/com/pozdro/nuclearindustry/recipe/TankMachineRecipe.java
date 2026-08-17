package com.pozdro.nuclearindustry.recipe;

import java.util.List;

public class TankMachineRecipe implements MachineRecipe{

    private final List<ItemIngredient> inputs;
    private final List<FluidIngredient> fluidInputs;
    private final List<ItemIngredient> outputs;
    private final List<FluidIngredient> fluidOutputs;
    private final double energyNeeded;
    private final int processingTime;

    public TankMachineRecipe(
            List<ItemIngredient> inputs,
            List<FluidIngredient> fluidInputs,
            List<ItemIngredient> outputs,
            List<FluidIngredient> fluidOutputs,
            double energyNeeded,
            int processingTime) {

        this.inputs = inputs;
        this.fluidInputs = fluidInputs;
        this.outputs = outputs;
        this.fluidOutputs = fluidOutputs;
        this.energyNeeded = energyNeeded;
        this.processingTime = processingTime;
    }

    public BasicMachineRecipe getOnlyBasicPart(){
        return new BasicMachineRecipe(inputs,outputs,energyNeeded,processingTime);
    }

    public List<ItemIngredient> inputs() {
        return inputs;
    }

    public List<FluidIngredient> fluidInputs() {
        return fluidInputs;
    }

    public List<ItemIngredient> outputs() {
        return outputs;
    }

    public List<FluidIngredient> fluidOutputs() {
        return fluidOutputs;
    }

    public double energyNeeded() {
        return energyNeeded;
    }

    public int processingTime() {
        return processingTime;
    }
}