package com.pozdro.nuclearindustry.tile.tankte;

import com.pozdro.nuclearindustry.tile.basicte.SlotType;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidTank;

public class TileTank {
    private final int tankID;
    private final TankType tankType;
    private final EnumFacing tankInteractionSide;
    private final int tankX;
    private final int tankY;
    private final int tankXSize;
    private final int tankYSize;
    private final FluidTank tank;

    public TileTank(int tankID, TankType tankType, EnumFacing tankInteractionSide, int tankX, int tankY, int tankXSize, int tankYSize, FluidTank tank) {
        this.tankID = tankID;
        this.tankType = tankType;
        this.tankInteractionSide = tankInteractionSide;
        this.tankX = tankX;
        this.tankY = tankY;
        this.tankXSize = tankXSize;
        this.tankYSize = tankYSize;
        this.tank = tank;
    }

    public FluidTank getTank() {
        return tank;
    }

    public int getTankYSize() {
        return tankYSize;
    }

    public int getTankXSize() {
        return tankXSize;
    }

    public int getTankY() {
        return tankY;
    }

    public int getTankX() {
        return tankX;
    }

    public EnumFacing getTankInteractionSide() {
        return tankInteractionSide;
    }

    public TankType getTankType() {
        return tankType;
    }

    public int getTankID() {
        return tankID;
    }
}
