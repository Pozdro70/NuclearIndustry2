package com.pozdro.nuclearindustry.fluid;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.item.ModCreativeTabs;
import com.pozdro.nuclearindustry.item.custom.ModFluidBucketItem;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.Sys;


import java.awt.Color;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = NuclearIndustry.MODID)
public class ModFluids {

    public static final BiMap<String, Fluid> modFluids = HashBiMap.create();
    private static final Map<String,Block> modBlockFluids = new HashMap<>();
    private static final List<Item> modFluidBuckets = new ArrayList<>();


    public static Fluid newFluid(String name, int color){
        ResourceLocation still =
                new ResourceLocation(NuclearIndustry.MODID, "blocks/still");

        ResourceLocation flowing =
                new ResourceLocation(NuclearIndustry.MODID, "blocks/flow");

        System.out.println(flowing.getPath());

        Fluid fluid = new Fluid(name,still,flowing,color);
        fluid.setUnlocalizedName(name);

        if(!FluidRegistry.registerFluid(fluid)){
            fluid=FluidRegistry.getFluid(name);
        }
        FluidRegistry.addBucketForFluid(fluid);

        modFluids.put(name,fluid);
        return fluid;
    }

    public static <T extends BlockFluidClassic> T newFluidBlock(T block){
        block.getFluid().setBlock(block);
        block.setRegistryName(NuclearIndustry.MODID,"fluid_"+modFluids.inverse().get(block.getFluid()));
        block.setTranslationKey(block.getRegistryName().toString());

        modBlockFluids.put(block.getFluid().getName(),block);
        return block;
    }

    public static final Fluid SULFURIC_ACID= newFluid("sulfuric_acid", 0xFFF2F0B0).setDensity(1800).setViscosity(1500);
    public static final BlockFluidClassic SULFURIC_ACID_BLOCK = newFluidBlock(new BlockFluidClassic(SULFURIC_ACID, Material.WATER));

    public static final Fluid COPPER_LEACHATE= newFluid("copper_leachate", 0xFF20AFA0).setDensity(1800).setViscosity(1500);
    public static final BlockFluidClassic COPPER_LEACHATE_BLOCK = newFluidBlock(new BlockFluidClassic(COPPER_LEACHATE, Material.WATER));

    public static final Fluid COPPER_SOLUTION= newFluid("copper_solution", 0xB818B8B0).setDensity(1800).setViscosity(1500);
    public static final BlockFluidClassic COPPER_SOLUTION_BLOCK = newFluidBlock(new BlockFluidClassic(COPPER_SOLUTION, Material.WATER));


    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        modBlockFluids.forEach((name,block)-> event.getRegistry().register(block));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        modFluidBuckets.forEach((item)-> ModelLoader.setCustomModelResourceLocation(item,0,
                new ModelResourceLocation(item.getRegistryName(), "inventory")));



        modBlockFluids.forEach((name, block) -> {
            ModelLoader.setCustomStateMapper(block, new StateMapperBase() {
                @Override
                protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
                    return new ModelResourceLocation(
                            NuclearIndustry.MODID + ":"+name,
                            "normal"
                    );
                }
            });
        });
    }


}
