package com.pozdro.nuclearindustry.block.tile;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.items.ItemStackHandler;

public interface IHasInventory {
    void dropInventory();

    ItemStackHandler getInventoryHandler();

    void onBlockActivatedNonRemote(EntityPlayer player);
}
