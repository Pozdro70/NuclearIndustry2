package com.pozdro.nuclearindustry.network;

import com.pozdro.nuclearindustry.tile.ISettableTank;
import com.pozdro.nuclearindustry.tile.tankte.TileTank;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TankSyncPacket implements IMessage {

    private BlockPos pos;
    private List<TileTank> tanks;

    // Required for Forge networking
    public TankSyncPacket() {
    }

    /**
     * Used when sending the packet from server -> client.
     */
    public TankSyncPacket(BlockPos pos, List<TileTank> tanks) {
        this.pos = pos;
        this.tanks = tanks;
    }

    /**
     * Convenience constructor if you have the tanks first.
     */
    public TankSyncPacket(List<TileTank> tanks, BlockPos pos) {
        this.pos = pos;
        this.tanks = new ArrayList<>();

        // IMPORTANT:
        // Add ALL tanks, including empty tanks.
        // Empty tanks need to be synchronized too.
        for (TileTank tileTank : tanks) {
            this.tanks.add(tileTank);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {

        PacketBuffer pb = new PacketBuffer(buf);

        // Block position
        pb.writeLong(pos.toLong());

        // Number of tanks
        pb.writeInt(tanks.size());

        for (TileTank tileTank : tanks) {

            // Tank ID
            pb.writeInt(tileTank.getTankID());

            FluidStack fluid = tileTank.getTank().getFluid();

            // Does the tank contain fluid?
            pb.writeBoolean(fluid != null);

            if (fluid != null) {
                NBTTagCompound fluidNBT = fluid.writeToNBT(new NBTTagCompound());
                pb.writeCompoundTag(fluidNBT);
            }

            // Tank capacity
            pb.writeInt(tileTank.getTank().getCapacity());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        PacketBuffer pb = new PacketBuffer(buf);

        // Block position
        pos = BlockPos.fromLong(pb.readLong());

        // Number of tanks
        int size = pb.readInt();

        tanks = new ArrayList<>();

        for (int i = 0; i < size; i++) {

            // Tank ID
            int tankID = pb.readInt();

            // Is there fluid?
            boolean present = pb.readBoolean();

            FluidStack fluid = null;

            if (present) {
                NBTTagCompound nbt = null;
                try {
                    nbt = pb.readCompoundTag();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                if (nbt != null) {
                    try {
                        fluid = FluidStack.loadFluidStackFromNBT(nbt);
                    } catch (Exception e) {
                        fluid = null;
                    }
                }
            }

            // Tank capacity
            int tankCapacity = pb.readInt();

            /*
             * We only need the tank ID and fluid on the client.
             *
             * The other TileTank properties are placeholders because
             * the real tank configuration already exists in the
             * client-side TileEntity.
             */
            tanks.add(
                    new TileTank(
                            tankID,
                            null,
                            null,
                            0,
                            0,
                            0,
                            0,
                            new FluidTank(fluid, tankCapacity)
                    )
            );
        }
    }

    public static class Handler
            implements IMessageHandler<TankSyncPacket, IMessage> {

        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(
                TankSyncPacket message,
                MessageContext ctx) {

            Minecraft.getMinecraft().addScheduledTask(() -> {

                if (Minecraft.getMinecraft().world == null) {
                    return;
                }

                TileEntity te =
                        Minecraft.getMinecraft()
                                .world
                                .getTileEntity(message.pos);

                if (te instanceof ISettableTank) {

                    ISettableTank settableTank =
                            (ISettableTank) te;

                    settableTank.setFluidsInTanks(message.tanks);
                }
            });

            return null;
        }
    }
}