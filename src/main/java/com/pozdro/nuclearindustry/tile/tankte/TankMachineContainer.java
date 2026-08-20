package com.pozdro.nuclearindustry.tile.tankte;

import com.pozdro.nuclearindustry.network.ModPacketHandler;
import com.pozdro.nuclearindustry.network.TankSyncPacket;
import com.pozdro.nuclearindustry.tile.IHasInventory;
import com.pozdro.nuclearindustry.tile.IHasProgressAndEnergy;
import com.pozdro.nuclearindustry.tile.ISettableTank;
import com.pozdro.nuclearindustry.tile.basicte.TileSlot;
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

import java.util.ArrayList;
import java.util.List;

public class TankMachineContainer<
        T extends TileEntity
                & IHasInventory
                & IHasProgressAndEnergy
                & ISettableTank> extends Container {

    private final T tile;

    private int lastProgress = -1;
    private int lastEnergy = -1;
    private int lastMaxProgress = -1;


    private final List<TileTank> lastFluids = new ArrayList<>();

    private final int playerInvYOffset;

    public TankMachineContainer(
            InventoryPlayer playerInv,
            T tile,
            List<TileSlot> guiSlots,
            int playerInvYOffset) {

        this.tile = tile;
        this.playerInvYOffset = playerInvYOffset;

        lastProgress = tile.getProgress();
        lastMaxProgress = tile.getMaxProgress();

        for (TileTank tileTank : tile.getFluidTanks()) {

            FluidStack fluid = tileTank.getTank().getFluid();

            FluidTank copiedTank;

            if (fluid == null) {
                copiedTank = new FluidTank(tileTank.getTank().getCapacity());
            } else {
                copiedTank = new FluidTank(
                        fluid.copy(),
                        tileTank.getTank().getCapacity()
                );
            }

            TileTank copiedTileTank = new TileTank(
                    tileTank.getTankID(),
                    tileTank.getTankType(),
                    tileTank.getTankInteractionSide(),
                    tileTank.getTankX(),
                    tileTank.getTankY(),
                    tileTank.getTankXSize(),
                    tileTank.getTankYSize(),
                    copiedTank
            );

            lastFluids.add(copiedTileTank);
        }

        ItemStackHandler inventoryHandler =
                tile.getInventoryHandler();

        guiSlots.forEach(tileSlot ->
                addSlotToContainer(
                        new SlotItemHandler(
                                inventoryHandler,
                                tileSlot.getSlotID(),
                                tileSlot.getSlotXCord(),
                                tileSlot.getSlotYCord()
                        )
                )
        );


        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {

                addSlotToContainer(
                        new Slot(
                                playerInv,
                                col + row * 9 + 9,
                                8 + col * 18,
                                84 + playerInvYOffset + row * 18
                        )
                );
            }
        }


        for (int col = 0; col < 9; col++) {

            addSlotToContainer(
                    new Slot(
                            playerInv,
                            col,
                            8 + col * 18,
                            142 + playerInvYOffset
                    )
            );
        }
    }

    @Override
    public void detectAndSendChanges() {

        super.detectAndSendChanges();


        if (tile.getProgress() != lastProgress) {

            for (IContainerListener listener : this.listeners) {
                listener.sendWindowProperty(
                        this,
                        0,
                        tile.getProgress()
                );
            }

            lastProgress = tile.getProgress();
        }

        if (tile.getMaxProgress() != lastMaxProgress) {

            for (IContainerListener listener : this.listeners) {
                listener.sendWindowProperty(
                        this,
                        2,
                        tile.getMaxProgress()
                );
            }

            lastMaxProgress = tile.getMaxProgress();
        }

        List<TileTank> curTanks = tile.getFluidTanks();

        boolean anyTankChanged = false;

        for (TileTank curTank : curTanks) {

            int id = curTank.getTankID();

            if (id >= lastFluids.size()) {
                anyTankChanged = true;
                break;
            }

            FluidStack current =
                    curTank.getTank().getFluid();

            FluidStack last =
                    lastFluids.get(id).getTank().getFluid();

            if (current == null || last == null) {

                if (current != last) {
                    anyTankChanged = true;
                    break;
                }

            } else if (!current.isFluidEqual(last)
                    || current.amount != last.amount) {

                anyTankChanged = true;
                break;
            }
        }

        if (anyTankChanged) {

            lastFluids.clear();


            for (TileTank tileTank : curTanks) {

                FluidStack fluid =
                        tileTank.getTank().getFluid();

                FluidTank copiedTank;

                if (fluid == null) {

                    copiedTank =
                            new FluidTank(
                                    tileTank.getTank().getCapacity()
                            );

                } else {

                    copiedTank =
                            new FluidTank(
                                    fluid.copy(),
                                    tileTank.getTank().getCapacity()
                            );
                }

                TileTank copiedTileTank =
                        new TileTank(
                                tileTank.getTankID(),
                                tileTank.getTankType(),
                                tileTank.getTankInteractionSide(),
                                tileTank.getTankX(),
                                tileTank.getTankY(),
                                tileTank.getTankXSize(),
                                tileTank.getTankYSize(),
                                copiedTank
                        );

                lastFluids.add(copiedTileTank);
            }

            for (IContainerListener listener : listeners) {

                if (listener instanceof EntityPlayerMP) {

                    ModPacketHandler.INSTANCE.sendTo(
                            new TankSyncPacket(
                                    tile.getPos(),
                                    lastFluids
                            ),
                            (EntityPlayerMP) listener
                    );
                }
            }
        }


        int energy =
                (int) tile.getEnergySink().getEnergyStored();

        if (energy != lastEnergy) {

            for (IContainerListener listener : listeners) {

                listener.sendWindowProperty(
                        this,
                        1,
                        energy
                );
            }

            lastEnergy = energy;
        }
    }

    @Override
    public void updateProgressBar(int id, int data) {

        if (id == 0) {
            tile.setProgress(data);
        }

        if (id == 1) {
            tile.setClientEnergy(data);
        }

        if (id == 2) {
            tile.setMaxProgress(data);
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {

        return tile.getWorld()
                .getTileEntity(tile.getPos()) == tile;
    }

    @Override
    public ItemStack transferStackInSlot(
            EntityPlayer player,
            int index) {

        ItemStack result = ItemStack.EMPTY;

        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {

            ItemStack stackInSlot = slot.getStack();

            result = stackInSlot.copy();

            int machineSlotCount =
                    tile.getInventoryHandler().getSlots();

            int playerInvStart =
                    machineSlotCount;

            int playerInvEnd =
                    playerInvStart + 27;

            int hotbarStart =
                    playerInvEnd;

            int hotbarEnd =
                    hotbarStart + 9;

            if (index < machineSlotCount) {

                if (!mergeItemStack(
                        stackInSlot,
                        playerInvStart,
                        hotbarEnd,
                        true)) {

                    return ItemStack.EMPTY;
                }

            } else if (index < playerInvEnd) {

                if (!mergeItemStack(
                        stackInSlot,
                        0,
                        machineSlotCount,
                        false)) {

                    if (!mergeItemStack(
                            stackInSlot,
                            hotbarStart,
                            hotbarEnd,
                            false)) {

                        return ItemStack.EMPTY;
                    }
                }

            } else if (index < hotbarEnd) {

                if (!mergeItemStack(
                        stackInSlot,
                        0,
                        machineSlotCount,
                        false)) {

                    if (!mergeItemStack(
                            stackInSlot,
                            playerInvStart,
                            playerInvEnd,
                            false)) {

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


        listener.sendWindowProperty(
                this,
                0,
                tile.getProgress()
        );


        if (listener instanceof EntityPlayerMP) {

            ModPacketHandler.INSTANCE.sendTo(
                    new TankSyncPacket(
                            tile.getPos(),
                            tile.getFluidTanks()
                    ),
                    (EntityPlayerMP) listener
            );
        }


        int energy =
                (int) tile.getEnergySink().getEnergyStored();

        listener.sendWindowProperty(
                this,
                1,
                energy
        );

        listener.sendWindowProperty(
                this,
                2,
                tile.getMaxProgress()
        );
    }
}