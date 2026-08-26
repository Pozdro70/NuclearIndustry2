package com.pozdro.nuclearindustry.item;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.item.custom.ScrewdriverItem;
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


    public static final Item COPPER_ORE_DUST=newItem(new Item()
            .setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB).setMaxStackSize(64),"copper_ore_dust");

    public static final Item GOLD_ORE_DUST=newItem(new Item()
            .setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB).setMaxStackSize(64),"gold_ore_dust");

    public static final Item IRON_ORE_DUST=newItem(new Item()
            .setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB).setMaxStackSize(64),"iron_ore_dust");

    public static final Item LEAD_ORE_DUST=newItem(new Item()
            .setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB).setMaxStackSize(64),"lead_ore_dust");

    public static final Item SILVER_ORE_DUST=newItem(new Item()
            .setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB).setMaxStackSize(64),"silver_ore_dust");

    public static final Item TIN_ORE_DUST=newItem(new Item()
            .setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB).setMaxStackSize(64),"tin_ore_dust");

    public static final Item URANIUM_ORE_DUST=newItem(new Item()
            .setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB).setMaxStackSize(64),"uranium_ore_dust");

    public  static final Item SCREWDRIVER = newItem(new ScrewdriverItem()
            .setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB).setMaxStackSize(1), "screwdriver");



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
