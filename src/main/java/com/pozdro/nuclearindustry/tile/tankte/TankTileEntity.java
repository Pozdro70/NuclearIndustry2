package com.pozdro.nuclearindustry.tile.tankte;

import com.pozdro.nuclearindustry.tile.ISettableTank;
import com.pozdro.nuclearindustry.tile.basicte.BasicMachineGuiHandler;
import com.pozdro.nuclearindustry.tile.basicte.BasicTileEntity;
import com.pozdro.nuclearindustry.tile.basicte.TileSlot;
import ic2.api.energy.prefab.BasicSink;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public  abstract class TankTileEntity extends BasicTileEntity implements ISettableTank {

    protected TankTileEntity(int slotCount, List<TileSlot> slots,List<TileTank> tanks, String tileName, int guiID) {
        super(slotCount, slots, tileName, guiID);
        this.tanks=tanks;
    }

    List<TileTank> tanks;

    private IFluidHandler getFluidHandlerForSide(@Nullable EnumFacing side){
        return new IFluidHandler() {
            @Override
            public IFluidTankProperties[] getTankProperties() {
                for (TileTank tileTank : tanks) {
                    if(tileTank.getTankType()!=TankType.DISABLED){
                        if(side==tileTank.getTankInteractionSide()){
                            return tileTank.getTank().getTankProperties();
                        }
                    }
                }
                return new IFluidTankProperties[0];
            }

            @Override
            public int fill(FluidStack resource, boolean doFill) {

                for (TileTank tileTank : tanks) {
                    if(tileTank.getTankType()!=TankType.DISABLED && tileTank.getTankType()==TankType.INPUT_TANK){
                        if(side==tileTank.getTankInteractionSide()){
                            if (tileTank.getTank().canFillFluidType(resource)){

                                return tileTank.getTank().fill(resource,doFill);
                            }
                        }
                    }
                }

                return 0;
            }

            @Nullable
            @Override
            public FluidStack drain(FluidStack resource, boolean doDrain) {
                for (TileTank tileTank : tanks) {
                    if(tileTank.getTankType()!=TankType.DISABLED && tileTank.getTankType()==TankType.OUTPUT_TANK){
                        if(side==tileTank.getTankInteractionSide()){
                            return tileTank.getTank().drain(resource,doDrain);

                        }
                    }
                }

                return null;
            }

            @Nullable
            @Override
            public FluidStack drain(int maxDrain, boolean doDrain) {
                for (TileTank tileTank : tanks) {
                    if(tileTank.getTankType()!=TankType.DISABLED && tileTank.getTankType()==TankType.OUTPUT_TANK){
                        if(side==tileTank.getTankInteractionSide()){
                            return tileTank.getTank().drain(maxDrain,doDrain);

                        }
                    }
                }

                return null;
            }
        };
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability== CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY){
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidHandlerForSide(facing));
        }

        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability==CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;

        return super.hasCapability(capability, facing);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {

        for (TileTank tileTank : tanks) {
            NBTTagCompound tankInTag = new NBTTagCompound();
            tileTank.getTank().writeToNBT(tankInTag);
            compound.setTag("Tank"+tileTank.getTankID(), tankInTag);
        }

        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {

        for (TileTank tileTank : tanks) {
            tileTank.getTank().readFromNBT(compound.getCompoundTag("Tank"+tileTank.getTankID()));
        }

        super.readFromNBT(compound);
    }

    @Override
    public void setFluidsInTanks(List<TileTank> tileTanks) {
        for (TileTank tileTank : tileTanks) {
            for (TileTank tank : tanks) {
                if(tileTank.getTankID()==tank.getTankID()){
                    tank.getTank().setFluid(tileTank.getTank().getFluid());
                }
            }
        }
    }

    @Override
    public List<TileTank> getFluidTanks() {
        return tanks;
    }
}
