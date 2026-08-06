package com.pozdro.nuclearindustry.network;

import com.pozdro.nuclearindustry.block.tile.ISettableTank;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TankSyncPacket implements IMessage {
    private BlockPos pos;
    private Map<Integer,FluidStack> tanks;

    public TankSyncPacket(){}

    public TankSyncPacket(BlockPos pos, Map<Integer, FluidStack> tanks){
        this.pos = pos;
        this.tanks=tanks;
    }

    public TankSyncPacket(Map<Integer, FluidTank> fluidTanks, BlockPos pos) {
        //parameters other way around because java thinks this is same constructor as above
        this.pos = pos;
        this.tanks = new HashMap<>();

        fluidTanks.forEach((tankID, fluidTank) -> {
            FluidStack fluid = fluidTank.getFluid();
            this.tanks.put(tankID, fluid == null ? null : fluid.copy());
        });
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);

        pb.writeLong(pos.toLong());
        pb.writeInt(tanks.size());

        tanks.forEach((tankID,fluid)->{
            pb.writeInt(tankID);

            pb.writeBoolean(fluid!=null);

            if(fluid!=null){
                pb.writeCompoundTag(fluid.writeToNBT(new NBTTagCompound()));
            }
        });
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);

        pos=BlockPos.fromLong(pb.readLong());

        int size = pb.readInt();
        tanks = new HashMap<>();

        for (int i = 0; i < size; i++) {
            Integer tankID = pb.readInt();

            boolean present = pb.readBoolean();

            FluidStack fluid=null;

            if(present){
                try{
                    fluid = FluidStack.loadFluidStackFromNBT(pb.readCompoundTag());
                }
                catch (Exception e){
                    fluid = null;
                }
            }

            tanks.put(tankID,fluid);

        }

    }

    public static class Handler implements IMessageHandler<TankSyncPacket,IMessage>{

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(TankSyncPacket message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(()->{
                TileEntity te = Minecraft.getMinecraft().world.getTileEntity(message.pos);
                if(te instanceof ISettableTank){
                    ISettableTank settableTank  = (ISettableTank) te;

                    message.tanks.forEach((tankID,fluid)->{
                        settableTank.setFluidInTank(fluid,tankID);
                    });
                }
            });
            return null;
        }
    }

}
