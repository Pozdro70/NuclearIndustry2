package com.pozdro.nuclearindustry.item;

import com.pozdro.nuclearindustry.NuclearIndustry;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = NuclearIndustry.MODID)
public class ModItems {
    private static final List<Item> modItems = new ArrayList<>();

    public static <T extends Item> T newItem(T item, String regName){
        item.setRegistryName(NuclearIndustry.MODID,regName);
        item.setTranslationKey(NuclearIndustry.MODID + "." + regName);
        modItems.add(item);
        return item;
    }


    public static final Item COPPEROREDUST=newItem(new Item()
            .setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB).setMaxStackSize(64),"copperoredust");

    public static final Item GOLDOREDUST=newItem(new Item()
            .setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB).setMaxStackSize(64),"goldoredust");

    public static final Item IRONOREDUST=newItem(new Item()
            .setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB).setMaxStackSize(64),"ironoredust");

    public static final Item LEADOREDUST=newItem(new Item()
            .setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB).setMaxStackSize(64),"leadoredust");

    public static final Item SILVEROREDUST=newItem(new Item()
            .setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB).setMaxStackSize(64),"silveroredust");

    public static final Item TINOREDUST=newItem(new Item()
            .setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB).setMaxStackSize(64),"tinoredust");

    public static final Item URANIUMOREDUST=newItem(new Item()
            .setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB).setMaxStackSize(64),"uraniumoredust");



    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(modItems.toArray(new Item[0]));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        modItems.forEach((item)-> ModelLoader.setCustomModelResourceLocation(item,0,
                new ModelResourceLocation(item.getRegistryName(), "inventory")));
    }


}
