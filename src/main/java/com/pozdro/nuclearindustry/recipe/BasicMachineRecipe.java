package com.pozdro.nuclearindustry.recipe;

import net.minecraft.item.ItemStack;
import java.util.Map;

public class BasicMachineRecipe {

    public Map<Integer, Map.Entry<ItemStack, Integer>> getInputs() {
        return inputs;
    }

    public Map<Integer, Map.Entry<ItemStack, Integer>> getOutputs() {
        return outputs;
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

    private final Map<Integer, Map.Entry<ItemStack, Integer>> inputs;
    private final Map<Integer, Map.Entry<ItemStack,Integer>> outputs;
    private double energyNeeded;
    private int processingTime;

    public BasicMachineRecipe(Map<Integer, Map.Entry<ItemStack, Integer>> inputs, Map<Integer, Map.Entry<ItemStack, Integer>> outputs,
                              double energyNeeded,int processingTime){

        this.inputs = inputs;
        this.outputs = outputs;
        this.energyNeeded = energyNeeded;
        this.processingTime = processingTime;
    }

}
