package com.pozdro.nuclearindustry.item.custom;

import com.pozdro.nuclearindustry.ModSounds;
import com.pozdro.nuclearindustry.tile.IHasPorts;
import com.pozdro.nuclearindustry.tile.basicte.PortType;
import ic2.api.item.IC2Items;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;

public class ScrewdriverItem extends Item {


    public ScrewdriverItem(){
        super();
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos position, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        TileEntity te = world.getTileEntity(position);
        if(te instanceof IHasPorts){
            if(!world.isRemote){
                IHasPorts portTile = (IHasPorts) te;
                switch(portTile.getPortFromSide(side)){
                    case NONE_PORT:
                        portTile.setPort(side, PortType.INPUT_PORT);
                        break;
                    case INPUT_PORT:
                        portTile.setPort(side, PortType.OUTPUT_PORT);
                        break;
                    case OUTPUT_PORT:
                        portTile.setPort(side, PortType.NONE_PORT);
                        break;
                }
                world.playSound(null, position, ModSounds.SCREWDRIVER_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }
}
