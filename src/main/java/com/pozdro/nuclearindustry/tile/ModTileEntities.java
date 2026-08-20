package com.pozdro.nuclearindustry.tile;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.tile.tiles.GrinderTileEntity;
import com.pozdro.nuclearindustry.tile.tiles.LeacherTileEntity;
import com.pozdro.nuclearindustry.tile.tiles.PurifierPressTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModTileEntities {
    public static void registerTileEntities(){ //FMLPreInit

        GameRegistry.registerTileEntity(
                LeacherTileEntity.class,
                new ResourceLocation(NuclearIndustry.MODID, "leacher")
        );

        GameRegistry.registerTileEntity(
                GrinderTileEntity.class,
                new ResourceLocation(NuclearIndustry.MODID, "grinder")
        );

        GameRegistry.registerTileEntity(
                PurifierPressTileEntity.class,
                new ResourceLocation(NuclearIndustry.MODID, "purifierpress")
        );
    }
}
