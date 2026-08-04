package com.pozdro.nuclearindustry;



import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = NuclearIndustry.MODID, name = Tags.MOD_NAME, version = Tags.VERSION)
public class NuclearIndustry {

    public static final String MODID= Tags.MOD_ID; //I like setting my modid like that, instead it is better to use Tags.MOD_ID everywhere.
    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Mod.EventHandler
    public void Init(FMLInitializationEvent event) {

    }


    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ModRecipes.removeRecipes();
    }

}
