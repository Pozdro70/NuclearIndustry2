package com.pozdro.nuclearindustry.network;

import com.pozdro.nuclearindustry.NuclearIndustry;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModPacketHandler {
    public static final SimpleNetworkWrapper INSTANCE =
            NetworkRegistry.INSTANCE.newSimpleChannel(NuclearIndustry.MODID);


    public static void registerPackets(){//FMLPreInit
        int id=0;
        INSTANCE.registerMessage(TankSyncPacket.Handler.class,TankSyncPacket.class,id++, Side.CLIENT);
    }
}
