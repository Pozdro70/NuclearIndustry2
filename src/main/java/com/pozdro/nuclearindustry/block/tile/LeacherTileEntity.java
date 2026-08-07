package com.pozdro.nuclearindustry.block.tile;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.fluid.ModFluids;
import ic2.api.energy.prefab.BasicSink;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class LeacherTileEntity extends TileEntity implements ITickable, IHasInventory,ISettableTank, IHasProgressAndEnergy {

    public static final int INVENTORY_SIZE = 11;

    public final ItemStackHandler inventory = new ItemStackHandler(INVENTORY_SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            markDirty();
        }
    };



    public final FluidTank tankIn = new FluidTank(10000) { //ID = 0
        @Override
        protected void onContentsChanged() {
            markDirty();
        }
    };

    public final FluidTank tankOut = new FluidTank(10000) { //ID = 1
        @Override
        protected void onContentsChanged() {
            markDirty();
        }
    };

    private static final Map<Integer, Map.Entry<Integer, Integer>> guiSlots = new HashMap<>();
    static {
        guiSlots.put(0, new AbstractMap.SimpleEntry<>(22, 18));
        guiSlots.put(1, new AbstractMap.SimpleEntry<>(22, 50));
        guiSlots.put(2, new AbstractMap.SimpleEntry<>(74, 21));
        guiSlots.put(3, new AbstractMap.SimpleEntry<>(105, 72));
        guiSlots.put(4, new AbstractMap.SimpleEntry<>(124, 72));
        guiSlots.put(5, new AbstractMap.SimpleEntry<>(129, 18));
        guiSlots.put(6, new AbstractMap.SimpleEntry<>(129, 50));

        //upgrade slots
        guiSlots.put(7, new AbstractMap.SimpleEntry<>(152, 21));
        guiSlots.put(8, new AbstractMap.SimpleEntry<>(152, 39));
        guiSlots.put(9, new AbstractMap.SimpleEntry<>(152, 57));
        guiSlots.put(10, new AbstractMap.SimpleEntry<>(152, 75));
    }

    private static final Map<Integer, Map.Entry<Integer, Integer>> tankSize = new HashMap<>();
    static {
        tankSize.put(0, new AbstractMap.SimpleEntry<>(48, 16));
        tankSize.put(1, new AbstractMap.SimpleEntry<>(48, 16));
    }

    private static final Map<Integer, Map.Entry<Integer, Integer>> tankPos = new HashMap<>();
    static {
        tankPos.put(0, new AbstractMap.SimpleEntry<>(47, 18));
        tankPos.put(1, new AbstractMap.SimpleEntry<>(105, 18));
    }


    public static void renderGUI(){//FMLInit
        NetworkRegistry.INSTANCE.registerGuiHandler(NuclearIndustry.instance, new BasicMachineGuiHandler<>(
                guiSlots,
                new ResourceLocation(NuclearIndustry.MODID, "textures/gui/leachergui.png"),
                LeacherTileEntity.class,
                "container.leacher",
                true,
                26,
                47,
                65,39,
                176,0,
                176,
                181,
                15,
                tankSize,
                tankPos,
                9 ,20,
                56
        ));
        GameRegistry.registerTileEntity(LeacherTileEntity.class, new ResourceLocation(NuclearIndustry.MODID, "leacher"));
    }


    private int progress = 0;
    private final int maxProgress = 100;

    @Override
    public void dropInventory() {
        InventoryHelper.dropInventoryItems(world, pos, new InventoryHandlerWrapper(
                inventory,
                "container.leacher")
        );
    }

    @Override
    public ItemStackHandler getInventoryHandler() {
        return inventory;
    }

    public final BasicSink energy = new BasicSink(this,10000,1);
    private static int clientEnergy;

    @Override
    public BasicSink getEnergySink() {
        return energy;
    }

    @Override
    public void setClientEnergy(int energy) {
        clientEnergy=energy;
    }

    @Override
    public int getClientEnergy() {
        return clientEnergy;
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if(!world.isRemote){energy.onLoad();}
    }

    @Override
    public void invalidate() {
        if(!world.isRemote){energy.invalidate();}

        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        if(!world.isRemote){energy.onChunkUnload();}

        super.onChunkUnload();
    }

    @Override
    public void update() {
        if(world.isRemote) return;
        //logic

        tankOut.setFluid(new FluidStack(FluidRegistry.WATER,tankOut.getCapacity()));
        tankIn.setFluid(new FluidStack(ModFluids.URANIUM_HEXAFLUORIDE,tankIn.getCapacity()));
        //progress++;

        progress=maxProgress;

        //if(progress>=maxProgress){progress=0;}
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability== CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
        if(capability== CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;

        return super.hasCapability(capability,facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setTag("Inventory", inventory.serializeNBT());

        NBTTagCompound tankInTag = new NBTTagCompound();
        tankIn.writeToNBT(tankInTag);
        compound.setTag("TankIn", tankInTag);

        NBTTagCompound tankOutTag = new NBTTagCompound();
        tankOut.writeToNBT(tankOutTag);
        compound.setTag("TankOut", tankOutTag);

        compound.setInteger("Progress", progress);

        energy.writeToNBT(compound);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        inventory.deserializeNBT(compound.getCompoundTag("Inventory"));

        tankIn.readFromNBT(compound.getCompoundTag("TankIn"));

        tankOut.readFromNBT(compound.getCompoundTag("TankOut"));

        progress = compound.getInteger("Progress");

        energy.readFromNBT(compound);
    }

    @Override
    public int getProgress() { return progress; }

    @Override
    public int getMaxProgress() { return maxProgress; }

    @Override
    public void setProgress(int p) { progress = p; }

    /*
    public FluidTank getTank(int tankID) {
        switch (tankID){
            case 0:
                return tankIn;
            case 1:
                return tankOut;
            default:
                return null;
        }

    }
     */


    @Override
    public void setFluidInTank(FluidStack fluidStack, int tankID) {
        switch (tankID){
            case 0:
                tankIn.setFluid(fluidStack);break;
            case 1:
                tankOut.setFluid(fluidStack);break;

        }
    }

    @Override
    public Map<Integer, FluidTank> getFluidTanks() {
        Map<Integer, FluidTank> fluidTanks = new HashMap<>();

        fluidTanks.put(0,tankIn);
        fluidTanks.put(1,tankOut);

        return fluidTanks;
    }


}
