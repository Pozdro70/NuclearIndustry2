package com.pozdro.nuclearindustry.block.tile;

import com.pozdro.nuclearindustry.network.ModPacketHandler;
import com.pozdro.nuclearindustry.network.TankSyncPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.HashMap;
import java.util.Map;

public class BasicMachineContainer<T extends TileEntity & IHasInventory & IHasProgress & ISettableTank> extends Container {

    private final T tile;
    private int lastProgress=-1;
    private final Map<Integer, FluidStack> lastFluids = new HashMap<>();
    private final int playerInvYOffset;

    public BasicMachineContainer(InventoryPlayer playerInv, T tile, Map<Integer ,Map.Entry<Integer,Integer>> guiSlots, int playerInvYOffset){
        this.tile=tile;
        lastProgress = tile.getProgress(); //we are not always sending first packet
        this.playerInvYOffset = playerInvYOffset;

        tile.getFluidTanks().forEach((id, tank) -> {
            FluidStack fluid = tank.getFluid();
            lastFluids.put(id, fluid == null ? null : fluid.copy());
        });

        ItemStackHandler inventoryHandler = tile.getInventoryHandler();

        //guiSlots works like that: Map<slotID,Map.Entry<slotX,slotY>>
        guiSlots.forEach((slotID,slotPos)->{
            addSlotToContainer(new SlotItemHandler(inventoryHandler,slotID,slotPos.getKey(),slotPos.getValue()));
        });

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



        //if any data updated, sync data
        if(tile.getProgress() != lastProgress){
            for (IContainerListener listener : this.listeners) {
                listener.sendWindowProperty(this,0,tile.getProgress());
            }
            lastProgress=tile.getProgress();
        }

        Map<Integer, FluidTank> curTanks = tile.getFluidTanks();

        boolean anyTankChanged = false;

        for (Map.Entry<Integer, FluidTank> entry : curTanks.entrySet()) {
            int id = entry.getKey();
            FluidStack current = entry.getValue().getFluid();
            FluidStack last = lastFluids.get(id);

            if (current == null || last == null) {
                if (current != last) {
                    anyTankChanged = true;
                    break;
                }
            } else if (!current.isFluidEqual(last) || current.amount != last.amount) {
                anyTankChanged = true;
                break;
            }
        }

        if(anyTankChanged){
            lastFluids.clear();

            curTanks.forEach((id,tank)->{
                FluidStack fluid = tank.getFluid();
                lastFluids.put(id, fluid == null ? null : fluid.copy());
            });

            for (IContainerListener listener : listeners) {
                if (listener instanceof EntityPlayerMP) {
                    System.out.println("Sending tank sync");
                    ModPacketHandler.INSTANCE.sendTo(
                            new TankSyncPacket(tile.getPos(), lastFluids),
                            (EntityPlayerMP) listener
                    );
                }
            }
        }




    }

    @Override
    public void updateProgressBar(int id, int data) {
        if(id==0) tile.setProgress(data);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tile.getWorld().getTileEntity(tile.getPos()) == tile;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        //sends the first packet when opening gui
        listener.sendWindowProperty(this,0,tile.getProgress());

        if (listener instanceof EntityPlayerMP) {
            ModPacketHandler.INSTANCE.sendTo(
                    new TankSyncPacket(tile.getFluidTanks(),tile.getPos()),
                    (EntityPlayerMP) listener
            );

        }
    }
}
