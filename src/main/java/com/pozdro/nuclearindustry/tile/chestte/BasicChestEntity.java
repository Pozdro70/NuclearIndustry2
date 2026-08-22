package com.pozdro.nuclearindustry.tile.chestte;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.block.custom.BasicMachineBlock;
import com.pozdro.nuclearindustry.recipe.BasicMachineRecipe;
import com.pozdro.nuclearindustry.recipe.MachineRecipe;
import com.pozdro.nuclearindustry.tile.IHasInventory;
import com.pozdro.nuclearindustry.tile.basicte.InventoryHandlerWrapper;
import com.pozdro.nuclearindustry.tile.basicte.SlotType;
import com.pozdro.nuclearindustry.tile.basicte.TileSlot;
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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class BasicChestEntity extends TileEntity implements IHasInventory {

    private int slotCount=0;
    List<TileSlot> slots;
    private String tileName;
    private int guiID;

    private final ItemStackHandler inventory;

    private EnumFacing rotateSide(EnumFacing localSide) {
        EnumFacing facing =
                world.getBlockState(pos).getValue(BasicMachineBlock.FACING);

        if (localSide == EnumFacing.UP || localSide == EnumFacing.DOWN) {
            return localSide;
        }

        switch (facing) {
            case NORTH:
                return localSide;

            case EAST:
                return localSide.rotateY();

            case SOUTH:
                return localSide.rotateY().rotateY();

            case WEST:
                return localSide.rotateYCCW();

            default:
                return localSide;
        }
    }

    protected BasicChestEntity(int slotCount, List<TileSlot> slots, String tileName, int guiID){
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

                     if (tileSlot.getSlotID() != slot) {
                         continue;
                     }

                     if (tileSlot.getSlotType() == SlotType.OUTPUT_SLOT ||
                             tileSlot.getSlotType() == SlotType.DISABLED) {
                         return false;
                     }

                     if(tileSlot.getSlotType()==SlotType.FLUID_HANDLER_SLOT){
                         IFluidHandlerItem handler =
                                 stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
                         return handler != null;
                     }

                     if (tileSlot.getSlotType() == SlotType.UPGRADE_SLOT) {
                         return stack.getItem() instanceof IUpgradeItem;
                     }

                     if (tileSlot.getSlotType() == SlotType.INPUT_SLOT) {
                         /*
                         for (BasicMachineRecipe recipe : getRecipeList()) {
                             for (ItemIngredient input : recipe.inputs()) {

                                 if (input.slot() == slot) {
                                     return input.stack().isItemEqual(stack);
                                 }
                             }
                         }
                         return false;
                         */
                         return true;
                     }
                 }

                 return false;
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
            public ItemStack insertItem(
                    int slot,
                    @Nonnull ItemStack stack,
                    boolean simulate) {

                if (slot < 0 || slot >= inventory.getSlots()) {
                    return stack;
                }

                for (TileSlot tileSlot : slots) {

                    if (tileSlot.getSlotID() != slot) {
                        continue;
                    }

                    if (tileSlot.getSlotType() != SlotType.INPUT_SLOT) {
                        return stack;
                    }

                    EnumFacing worldSide =
                            rotateSide(tileSlot.getSlotInteractionSide());

                    if (worldSide != side) {
                        return stack;
                    }

                    return inventory.insertItem(slot, stack, simulate);
                }

                return stack;
            }

            @Nonnull
            @Override
            public ItemStack extractItem(
                    int slot,
                    int amount,
                    boolean simulate) {

                for (TileSlot tileSlot : slots) {

                    if (tileSlot.getSlotType() != SlotType.OUTPUT_SLOT) {
                        continue;
                    }

                    EnumFacing worldSide =
                            rotateSide(tileSlot.getSlotInteractionSide());

                    if (worldSide != side) {
                        continue;
                    }

                    ItemStack result = inventory.extractItem(
                            tileSlot.getSlotID(),
                            amount,
                            simulate
                    );

                    if (!result.isEmpty()) {
                        return result;
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


    @Override
    public void onLoad() {
        super.onLoad();

        if(!world.isRemote){
            //getSink().onLoad();
        }
    }

    @Override
    public void invalidate() {
        //if(!world.isRemote){getSink().invalidate();}

        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        //if(!world.isRemote){getSink().onChunkUnload();}

        super.onChunkUnload();
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setTag("Inventory", inventory.serializeNBT());

        return compound;
    }


    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        inventory.deserializeNBT(compound.getCompoundTag("Inventory"));
    }



    public abstract BasicChestGuiHandler<? extends BasicChestEntity> getGuiHandler();

    public List<? extends MachineRecipe> getRecipes() {
        return getRecipeList();
    }

    protected abstract List<BasicMachineRecipe> getRecipeList();

}
