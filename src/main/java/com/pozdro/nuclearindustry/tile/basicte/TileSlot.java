package com.pozdro.nuclearindustry.tile.basicte;

import net.minecraft.util.EnumFacing;

public class TileSlot {
    private final int slotID;
    private final SlotType slotType;
    private final EnumFacing slotInteractionSide;
    private int slotX;
    private int slotY;

    public TileSlot(int slotID, SlotType slotType, EnumFacing slotInteractionSide, int slotX, int slotY){
        this.slotID=slotID;
        this.slotType=slotType;
        this.slotInteractionSide=slotInteractionSide;
        this.slotX = slotX;
        this.slotY = slotY;
    }

    public EnumFacing getSlotInteractionSide() {
        return slotInteractionSide;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public int getSlotID() {
        return slotID;
    }

    public int getSlotYCord() {
        return slotY;
    }

    public int getSlotXCord() {
        return slotX;
    }

}
