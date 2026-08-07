package com.pozdro.nuclearindustry.recipe;

import net.minecraftforge.fluids.FluidStack;

public class FluidIngredient {
    private final int tankID;
    private final FluidStack stack;
    private final int probability;

    public FluidIngredient(int tankID, FluidStack stack, int probability) {
        this.tankID = tankID;
        this.stack = stack;
        this.probability = probability;
    }

    public int tankID() {
        return tankID;
    }

    public FluidStack stack() {
        return stack;
    }

    public int probability() {
        return probability;
    }
}