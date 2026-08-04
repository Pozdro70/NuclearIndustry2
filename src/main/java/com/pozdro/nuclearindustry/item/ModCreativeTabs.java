package com.pozdro.nuclearindustry.item;

import com.pozdro.nuclearindustry.NuclearIndustry;
import ic2.api.item.IC2Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ModCreativeTabs {
    public static final CreativeTabs NUCLEAR_INDUSTRY_MAIN_TAB = new CreativeTabs(NuclearIndustry.MODID) {
        @Override
        public ItemStack createIcon() {
            return IC2Items.getItem("te","reactor_chamber");
        }
    };

}
