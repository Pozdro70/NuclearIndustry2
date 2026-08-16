package com.pozdro.nuclearindustry.tile;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public interface IHasInventory {
    void dropInventory();

    ItemStackHandler getInventoryHandler();

    void onBlockActivatedNonRemote(EntityPlayer player, World world, BlockPos pos);
}
