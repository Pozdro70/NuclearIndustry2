package com.pozdro.nuclearindustry.recipe;

import net.minecraft.item.ItemStack;

public class ItemIngredient {

    private final int slot;
    private final ItemStack stack;
    private final int probability;

    public ItemIngredient(int slot, ItemStack stack, int probability) {
        this.slot = slot;
        this.stack = stack;
        this.probability = probability;
    }

    public int slot() {
        return slot;
    }

    public ItemStack stack() {
        return stack;
    }

    public int probability() {
        return probability;
    }
}