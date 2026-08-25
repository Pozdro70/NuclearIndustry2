package com.pozdro.nuclearindustry.tile;

import com.pozdro.nuclearindustry.tile.basicte.PortType;

import net.minecraft.util.EnumFacing;

public interface IHasPorts {
    PortType getPortFromSide(EnumFacing side);
    EnumFacing[] getFacesFromPort(PortType portType);
    void setPort(EnumFacing side, PortType port);
}
