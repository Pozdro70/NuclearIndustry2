package com.pozdro.nuclearindustry.tile;


import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.Map;

public interface ISettableTank {

    default void setFluidInTank(FluidStack fluidStack){
        setFluidInTank(fluidStack,0);
    };

    void setFluidInTank(FluidStack fluidStack, int tankID);

    Map<Integer, FluidTank> getFluidTanks();



}
