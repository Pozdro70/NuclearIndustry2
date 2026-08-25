package com.pozdro.nuclearindustry;

import com.pozdro.nuclearindustry.tile.basicte.PortRenderer;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = "nuclearindustry", value = Side.CLIENT)
public class ClientRegistryHandler {
    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        // rejstruje assety do portow
        event.getMap().registerSprite(PortRenderer.INPUT_PORT_TEX);
        event.getMap().registerSprite(PortRenderer.OUTPUT_PORT_TEX);
    }
}
