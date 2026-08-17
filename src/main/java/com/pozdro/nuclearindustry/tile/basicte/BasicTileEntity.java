package com.pozdro.nuclearindustry.tile.basicte;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.recipe.BasicMachineRecipe;
import com.pozdro.nuclearindustry.recipe.ItemIngredient;
import com.pozdro.nuclearindustry.tile.IHasInventory;
import com.pozdro.nuclearindustry.tile.IHasProgressAndEnergy;
import ic2.api.energy.prefab.BasicSink;
import ic2.api.upgrade.IUpgradeItem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class BasicTileEntity extends TileEntity implements IHasInventory, IHasProgressAndEnergy {

    private int slotCount=0;
    List<TileSlot> slots;
    private List<BasicMachineRecipe> recipes = new ArrayList<>();
    private String tileName;
    private int guiID;

    private final ItemStackHandler inventory;

    protected BasicTileEntity(int slotCount, List<TileSlot> slots, String tileName, int guiID){
        this.slotCount=slotCount;
        this.slots=slots;
        this.tileName = tileName;
        this.guiID = guiID;

         inventory = new ItemStackHandler(slotCount){
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

                for (TileSlot tileSlot : slots) {
                    if(tileSlot.getSlotID()==slot && tileSlot.getSlotType()==SlotType.UPGRADE_SLOT && stack.getItem() instanceof IUpgradeItem) return true;
                    if(tileSlot.getSlotID()==slot && tileSlot.getSlotType()==SlotType.DISABLED) return true;
                    if(tileSlot.getSlotID()==slot && tileSlot.getSlotType()==SlotType.INPUT_SLOT) return false;
                }

                for (BasicMachineRecipe recipe : recipes) {
                    for (ItemIngredient input : recipe.inputs()) {
                        if(input.slot()==slot){
                            return input.stack().isItemEqual(stack);
                        }
                    }
                }

                return true;
            }
    };


    }

    private IItemHandler getItemHandlerForSide(@Nullable EnumFacing side){
        return new IItemHandler() {
            @Override
            public int getSlots() {
                return slotCount;
            }

            @Nonnull
            @Override
            public ItemStack getStackInSlot(int slot) {
                return inventory.getStackInSlot(slot);
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                for (TileSlot tileSlot : slots) {
                    if(tileSlot.getSlotType()==SlotType.INPUT_SLOT && tileSlot.getSlotInteractionSide()==side && side!=null){
                        if(inventory.isItemValid(tileSlot.getSlotID(),stack))
                            return inventory.insertItem(tileSlot.getSlotID(),stack,simulate);
                    }
                }
                return stack;
            }

            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                for (TileSlot tileSlot : slots) {
                    if(tileSlot.getSlotType()==SlotType.OUTPUT_SLOT && tileSlot.getSlotInteractionSide()==side){
                        ItemStack fromSlot=inventory.extractItem(tileSlot.getSlotID(),amount,simulate);
                        if(!fromSlot.isEmpty()) return fromSlot;
                    }
                }

                return ItemStack.EMPTY;
            }

            @Override
            public int getSlotLimit(int slot) {
                return inventory.getSlotLimit(slot);
            }
        };
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability== CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(getItemHandlerForSide(facing));
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;

        return super.hasCapability(capability, facing);
    }

    @Override
    public void dropInventory() {
        InventoryHelper.dropInventoryItems(world, pos, new InventoryHandlerWrapper(
                inventory,
                "container."+tileName.toLowerCase())
        );
    }

    @Override
    public ItemStackHandler getInventoryHandler() {
        return inventory;
    }

    @Override
    public void onBlockActivatedNonRemote(EntityPlayer player, World world, BlockPos pos) {
        player.openGui(NuclearIndustry.instance, guiID, world, pos.getX(), pos.getY(), pos.getZ());
    }


    public abstract BasicSink getSink();

    private int clientEnergy;

    @Override
    public BasicSink getEnergySink() {
        return getSink();
    }

    @Override
    public void setClientEnergy(int cenergy) {
        clientEnergy=cenergy;
        if(clientEnergy>getSink().getCapacity()){
            clientEnergy= (int) getSink().getCapacity();
        }
    }

    @Override
    public int getClientEnergy() {
        return clientEnergy;
    }


    @Override
    public void onLoad() {
        super.onLoad();

        if(!world.isRemote){getSink().onLoad();}
    }

    @Override
    public void invalidate() {
        if(!world.isRemote){getSink().invalidate();}

        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        if(!world.isRemote){getSink().onChunkUnload();}

        super.onChunkUnload();
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public abstract int getProgress();

    @Override
    public abstract void setProgress(int progress);

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setTag("Inventory", inventory.serializeNBT());

        compound.setInteger("Progress", getProgress());

        getSink().writeToNBT(compound);
        return compound;
    }


    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        inventory.deserializeNBT(compound.getCompoundTag("Inventory"));

        setProgress(compound.getInteger("Progress"));

        getSink().readFromNBT(compound);
    }


    @Override
    public abstract int getMaxProgress();

    @Override
    public abstract void setMaxProgress(int data);

    public abstract BasicMachineGuiHandler<? extends BasicTileEntity> getGuiHandler();

}
