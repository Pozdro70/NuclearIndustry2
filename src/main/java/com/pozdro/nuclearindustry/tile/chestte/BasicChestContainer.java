package com.pozdro.nuclearindustry.tile.chestte;

import com.pozdro.nuclearindustry.tile.IHasInventory;
import com.pozdro.nuclearindustry.tile.basicte.TileSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.List;

public class BasicChestContainer<T extends TileEntity & IHasInventory> extends Container {

    private final T tile;
    private final int playerInvYOffset;


    public BasicChestContainer(InventoryPlayer playerInv, T tile, List<TileSlot> guiSlots, int playerInvYOffset){
        this.tile = tile;
        this.playerInvYOffset = playerInvYOffset;


        ItemStackHandler inventoryHandler = tile.getInventoryHandler();

        guiSlots.forEach(tileSlot -> addSlotToContainer(new SlotItemHandler(inventoryHandler,tileSlot.getSlotID(),
                tileSlot.getSlotXCord(), tileSlot.getSlotYCord())));

        //player inventory
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                addSlotToContainer(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 84+playerInvYOffset + row * 18));
        for (int col = 0; col < 9; col++)
            addSlotToContainer(new Slot(playerInv, col, 8 + col * 18, 142+playerInvYOffset));
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tile.getWorld().getTileEntity(tile.getPos()) == tile;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stackInSlot = slot.getStack();
            result = stackInSlot.copy();

            int machineSlotCount = tile.getInventoryHandler().getSlots();
            int playerInvStart = machineSlotCount;
            int playerInvEnd = playerInvStart + 27; //27-player inventory slot count
            int hotbarStart = playerInvEnd;
            int hotbarEnd = hotbarStart + 9; //9 hotbar slots

            if (index < machineSlotCount) {
                //clicked a tileEntity slot
                if (!mergeItemStack(stackInSlot, playerInvStart, hotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < playerInvEnd) {
                //clicked a player main inventory slot
                if (!mergeItemStack(stackInSlot, 0, machineSlotCount, false)) {
                    if (!mergeItemStack(stackInSlot, hotbarStart, hotbarEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (index < hotbarEnd) {
                //clicked a hotbar slot
                if (!mergeItemStack(stackInSlot, 0, machineSlotCount, false)) {
                    if (!mergeItemStack(stackInSlot, playerInvStart, playerInvEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (stackInSlot.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stackInSlot.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stackInSlot);
        }

        return result;
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
    }
}
