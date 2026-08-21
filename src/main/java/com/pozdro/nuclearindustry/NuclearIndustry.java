package com.pozdro.nuclearindustry;



import com.pozdro.nuclearindustry.command.CommandReloadResources;
import com.pozdro.nuclearindustry.tile.ModGuiHandler;
import com.pozdro.nuclearindustry.tile.ModTileEntities;
import com.pozdro.nuclearindustry.tile.tiles.GrinderTileEntity;
import com.pozdro.nuclearindustry.tile.tiles.LeacherTileEntity;
import com.pozdro.nuclearindustry.network.ModPacketHandler;
import com.pozdro.nuclearindustry.recipe.ModRecipes;
import com.pozdro.nuclearindustry.tile.tiles.PurifierPressTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = NuclearIndustry.MODID, name = Tags.MOD_NAME, version = Tags.VERSION,dependencies = "required-after:ic2")
public class NuclearIndustry {

    public static final String MODID = Tags.MOD_ID; //I like setting my modid like that, instead it is better to use Tags.MOD_ID everywhere.
    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    @Mod.Instance(NuclearIndustry.MODID)
    public static NuclearIndustry instance;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModPacketHandler.registerPackets();
        ModTileEntities.registerTileEntities();
    }

    @Mod.EventHandler
    public void Init(FMLInitializationEvent event) {
        //OreDictionary.registerOre("iron_ingot",ModItems.test);

        NetworkRegistry.INSTANCE.registerGuiHandler(
                NuclearIndustry.instance,
                new ModGuiHandler()
        );

        ModRecipes.addModRecipes();
    }


    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ModRecipes.removeRecipes();

    }


    //rejstracja komendy na reloadowanie resource bo mi klawiatura
    // nie dziala cos to komende zrobilem
    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandReloadResources());
    }
}
