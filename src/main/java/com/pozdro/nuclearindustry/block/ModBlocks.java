package com.pozdro.nuclearindustry.block;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.block.custom.BasicMachineBlock;
import com.pozdro.nuclearindustry.block.tile.tiles.LeacherTileEntity;
import com.pozdro.nuclearindustry.item.ModCreativeTabs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
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
import java.util.List;

@Mod.EventBusSubscriber(modid = NuclearIndustry.MODID)
public class ModBlocks {
    private static final List<Block> modBlocks = new ArrayList<>();
    private static final List<Item> modBlockItems = new ArrayList<>();

    public static <T extends Block> T newBlock(T block, String regName, int harvestLevel){
        block.setRegistryName(NuclearIndustry.MODID,regName);
        block.setTranslationKey(block.getRegistryName().toString());
        block.setHarvestLevel("pickaxe", harvestLevel);
        modBlocks.add(block);
        modBlockItems.add(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        return block;
    }

    public static final Block MACHINE_HULL=newBlock(new Block(Material.IRON)
            .setHardness(3.0F).setResistance(5.0F).setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB), "machinehull",1);

    public static final Block MACHINE_CASING=newBlock(new Block(Material.IRON)
            .setHardness(3.0F).setResistance(5.0F).setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB), "machinecasing",1);

    public static final Block COPPER_COIL=newBlock(new Block(Material.IRON)
            .setHardness(3.0F).setResistance(5.0F).setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB), "coppercoil",1);

    public static final Block MACHINE_BLOCK=newBlock(new Block(Material.IRON)
            .setHardness(3.0F).setResistance(5.0F).setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB), "machineblock",1);

    public static final Block LEACHER=newBlock(new BasicMachineBlock<>(Material.IRON, LeacherTileEntity::new)
            .setHardness(3.0F).setResistance(5.0F).setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB), "leacher",1);

    public static final Block GRINDER=newBlock(new BasicMachineBlock<>(Material.IRON,LeacherTileEntity::new)
            .setHardness(3.0F).setResistance(5.0F).setCreativeTab(ModCreativeTabs.NUCLEAR_INDUSTRY_MAIN_TAB), "grinder",1);


    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) { //registering ItemBlocks
        event.getRegistry().registerAll(modBlockItems.toArray(new Item[0]));
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(modBlocks.toArray(new Block[0]));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        modBlocks.forEach((block)->ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block),0,
                new ModelResourceLocation(block.getRegistryName(), "inventory")));
    }



}
