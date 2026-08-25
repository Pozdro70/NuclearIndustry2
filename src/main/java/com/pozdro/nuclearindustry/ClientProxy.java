package com.pozdro.nuclearindustry;

import com.pozdro.nuclearindustry.tile.basicte.PortRenderer;
import com.pozdro.nuclearindustry.tile.chestte.BasicChestEntity;
import com.pozdro.nuclearindustry.tile.tankte.TankTileEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy {

    public void init(FMLInitializationEvent event){
        //bindujemy uniwersalny FastTESR z PortRenderer.java do naszych klas TileEntity
        ClientRegistry.bindTileEntitySpecialRenderer(BasicChestEntity.class, new PortRenderer<>());
        //ClientRegistry.bindTileEntitySpecialRenderer(TankTileEntity.class, new PortRenderer<>()); <--- to odkomentujemy jak bede dzialac porty w TankTileEntity
    }
}
