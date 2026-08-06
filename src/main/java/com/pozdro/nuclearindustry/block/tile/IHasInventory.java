package com.pozdro.nuclearindustry.block.tile;

import net.minecraftforge.items.ItemStackHandler;

public interface IHasInventory {
    void dropInventory();

    ItemStackHandler getInventoryHandler();
}
