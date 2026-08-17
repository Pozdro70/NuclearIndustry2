package com.pozdro.nuclearindustry.tile.tiles;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.tile.basicte.BasicMachineGuiHandler;
import com.pozdro.nuclearindustry.tile.basicte.BasicTileEntity;
import com.pozdro.nuclearindustry.tile.basicte.SlotType;
import com.pozdro.nuclearindustry.tile.basicte.TileSlot;
import com.pozdro.nuclearindustry.tile.tankte.TankMachineGuiHandler;
import ic2.api.energy.prefab.BasicSink;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Arrays;
import java.util.List;

public class GrinderTileEntity extends BasicTileEntity implements ITickable {

    BasicSink sink = new BasicSink(this,10000,1);

    static List<TileSlot> guiSlots= Arrays.asList(
        new TileSlot(0, SlotType.INPUT_SLOT, EnumFacing.UP,27,28),
        new TileSlot(1,SlotType.INPUT_SLOT,EnumFacing.UP,47,28),
        new TileSlot(2,SlotType.INPUT_SLOT,EnumFacing.UP,67,28),
        new TileSlot(3,SlotType.OUTPUT_SLOT,EnumFacing.DOWN,104,47),
        new TileSlot(4,SlotType.OUTPUT_SLOT,EnumFacing.DOWN,124,47),
        new TileSlot(5,SlotType.UPGRADE_SLOT,152,21),
        new TileSlot(6,SlotType.UPGRADE_SLOT,152,39),
        new TileSlot(7,SlotType.UPGRADE_SLOT,152,57),
        new TileSlot(8,SlotType.UPGRADE_SLOT,152,75)

    );

    private int progress = 0;
    private int maxProgress = 100;


    public GrinderTileEntity(){
        super(9, guiSlots, "grinder", 1);
    }

    @Override
    public BasicSink getSink() {
        return sink;
    }

    @Override
    public int getProgress() {
        return progress;
    }

    @Override
    public void setProgress(int progress) {
        this.progress=progress;
    }

    @Override
    public int getMaxProgress() {
        return maxProgress;
    }

    @Override
    public void setMaxProgress(int data) {
        this.maxProgress=data;
    }

    @Override
    public BasicMachineGuiHandler<? extends BasicTileEntity> getGuiHandler() {
        return new BasicMachineGuiHandler<>(
                guiSlots,
                new ResourceLocation(NuclearIndustry.MODID, "textures/gui/grindergui.png"),
                GrinderTileEntity.class,
                "Grinder",
                true,
                26,
                37,
                30,46,
                176,0,
                176,
                181,
                15,
                9,25,
                56,
                1
        );
    }

    @Override
    public void update() {

    }


}
