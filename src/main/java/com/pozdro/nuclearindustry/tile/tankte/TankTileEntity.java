package com.pozdro.nuclearindustry.tile.tankte;

import com.pozdro.nuclearindustry.block.custom.BasicMachineBlock;
import com.pozdro.nuclearindustry.recipe.BasicMachineRecipe;
import com.pozdro.nuclearindustry.recipe.TankMachineRecipe;
import com.pozdro.nuclearindustry.tile.ISettableTank;
import com.pozdro.nuclearindustry.tile.basicte.BasicMachineGuiHandler;
import com.pozdro.nuclearindustry.tile.basicte.BasicTileEntity;
import com.pozdro.nuclearindustry.tile.basicte.TileSlot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public  abstract class TankTileEntity extends BasicTileEntity implements ISettableTank {

    protected TankTileEntity(int slotCount, List<TileSlot> slots, String tileName, int guiID) {
        super(slotCount, slots, tileName, guiID);
    }

    protected static List<TileTank> tanks;

    private List<TankMachineRecipe> frecipes = new ArrayList<>();

    protected EnumFacing rotateSide(EnumFacing side) {
        if (side == null || world == null) {
            return side;
        }

        EnumFacing facing =
                world.getBlockState(pos).getValue(BasicMachineBlock.FACING);

        if (side == EnumFacing.UP || side == EnumFacing.DOWN) {
            return side;
        }

        switch (facing) {
            case NORTH:
                return side;

            case EAST:
                return side.rotateY();

            case SOUTH:
                return side.getOpposite();

            case WEST:
                return side.rotateYCCW();

            default:
                return side;
        }
    }

    private IFluidHandler getFluidHandlerForSide(@Nullable EnumFacing side) {
        return new IFluidHandler() {

            @Override
            public IFluidTankProperties[] getTankProperties() {
                for (TileTank tileTank : tanks) {

                    if (tileTank.getTankType() == TankType.DISABLED) {
                        continue;
                    }

                    EnumFacing worldSide =
                            rotateSide(tileTank.getTankInteractionSide());

                    if (side == worldSide) {
                        return tileTank.getTank().getTankProperties();
                    }
                }

                return new IFluidTankProperties[0];
            }

            @Override
            public int fill(FluidStack resource, boolean doFill) {
                for (TileTank tileTank : tanks) {

                    if (tileTank.getTankType() != TankType.INPUT_TANK) {
                        continue;
                    }

                    EnumFacing worldSide =
                            rotateSide(tileTank.getTankInteractionSide());

                    if (side != worldSide) {
                        continue;
                    }

                    if (tileTank.getTank().canFillFluidType(resource)) {
                        return tileTank.getTank().fill(resource, doFill);
                    }
                }

                return 0;
            }

            @Nullable
            @Override
            public FluidStack drain(FluidStack resource, boolean doDrain) {
                for (TileTank tileTank : tanks) {

                    if (tileTank.getTankType() != TankType.OUTPUT_TANK) {
                        continue;
                    }

                    EnumFacing worldSide =
                            rotateSide(tileTank.getTankInteractionSide());

                    if (side != worldSide) {
                        continue;
                    }

                    return tileTank.getTank().drain(resource, doDrain);
                }

                return null;
            }

            @Nullable
            @Override
            public FluidStack drain(int maxDrain, boolean doDrain) {
                for (TileTank tileTank : tanks) {

                    if (tileTank.getTankType() != TankType.OUTPUT_TANK) {
                        continue;
                    }

                    EnumFacing worldSide =
                            rotateSide(tileTank.getTankInteractionSide());

                    if (side != worldSide) {
                        continue;
                    }

                    return tileTank.getTank().drain(maxDrain, doDrain);
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

    public abstract TankMachineGuiHandler<? extends TankTileEntity> getTankGuiHandler();

    @Override
    public BasicMachineGuiHandler<? extends BasicTileEntity> getGuiHandler() {
        return null; //to make TannkTiles not yell about this not there
    }

    @Override
    public List<TankMachineRecipe> getRecipes() {
        return frecipes;
    }

    public void setTankMachineRecipes(List<TankMachineRecipe> frecipes) {
        this.frecipes = frecipes;


    }

    @Override
    protected List<BasicMachineRecipe> getRecipeList() {

        return new ArrayList<>(frecipes);
    }
}
