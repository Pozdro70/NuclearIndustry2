package com.pozdro.nuclearindustry.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.Map;


public class FluidTankMachineRecipe {


    private final Map<Integer, Map.Entry<ItemStack, Integer>> inputs;

    public Map<Integer, Map.Entry<ItemStack, Integer>> getInputs() {
        return inputs;
    }

    public Map<Integer, Map.Entry<FluidStack, Integer>> getFluidInputs() {
        return fluidInputs;
    }

    public Map<Integer, Map.Entry<ItemStack, Integer>> getOutputs() {
        return outputs;
    }

    public Map<Integer, Map.Entry<FluidStack, Integer>> getFluidOutputs() {
        return fluidOutputs;
    }

    public double getEnergyNeeded() {
        return energyNeeded;
    }

    public void setEnergyNeeded(double energyNeeded) {
        this.energyNeeded = energyNeeded;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(int processingTime) {
        this.processingTime = processingTime;
    }

    private final Map<Integer,Map.Entry<FluidStack,Integer>> fluidInputs;
    private final Map<Integer, Map.Entry<ItemStack,Integer>> outputs;
    private final Map<Integer,Map.Entry<FluidStack,Integer>> fluidOutputs;
    private double energyNeeded;
    private int processingTime;

    public FluidTankMachineRecipe(Map<Integer, Map.Entry<ItemStack, Integer>> inputs, Map<Integer, Map.Entry<FluidStack, Integer>> fluidInputs,
                                  Map<Integer, Map.Entry<ItemStack, Integer>> outputs, Map<Integer, Map.Entry<FluidStack, Integer>> fluidOutputs,
                                  double energyNeeded, int processingTime){

        this.inputs = inputs;
        this.fluidInputs = fluidInputs;
        this.outputs = outputs;
        this.fluidOutputs = fluidOutputs;
        this.energyNeeded = energyNeeded;
        this.processingTime = processingTime;
    }

}
