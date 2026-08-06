package com.pozdro.nuclearindustry.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.ItemStackHandler;

public class InventoryHandlerWrapper implements IInventory {

    private final ItemStackHandler handler;
    private final String containerName;

    public InventoryHandlerWrapper(ItemStackHandler handler,String containerName){
        this.handler=handler;
        this.containerName = containerName;
    }

    @Override
    public int getSizeInventory() {
        return handler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < handler.getSlots(); i++) if (!handler.getStackInSlot(i).isEmpty()) return false;
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return handler.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return handler.extractItem(index,count,false);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return handler.extractItem(index,handler.getSlotLimit(index),false);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        handler.setStackInSlot(index, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {}

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < handler.getSlots(); i++) handler.setStackInSlot(i, ItemStack.EMPTY);
    }

    @Override
    public String getName() {
        return containerName;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(getName());
    }
}
