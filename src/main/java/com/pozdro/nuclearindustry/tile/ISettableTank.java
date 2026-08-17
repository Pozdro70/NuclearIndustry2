package com.pozdro.nuclearindustry.tile;


import com.pozdro.nuclearindustry.tile.tankte.TileTank;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.List;
import java.util.Map;

public interface ISettableTank {

    void setFluidsInTanks(List<TileTank> tileTanks);

    List<TileTank> getFluidTanks();



}
