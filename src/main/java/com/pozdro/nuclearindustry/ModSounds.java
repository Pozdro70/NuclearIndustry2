package com.pozdro.nuclearindustry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = NuclearIndustry.MODID)
public class ModSounds {

    public static SoundEvent SCREWDRIVER_USE;

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        SCREWDRIVER_USE = registerSound("screwdriver_use", event);
    }

    private static SoundEvent registerSound(String soundName, RegistryEvent.Register<SoundEvent> event) {
        ResourceLocation location = new ResourceLocation(NuclearIndustry.MODID, soundName);
        SoundEvent sound = new SoundEvent(location).setRegistryName(location);
        event.getRegistry().register(sound);
        return sound;
    }
}