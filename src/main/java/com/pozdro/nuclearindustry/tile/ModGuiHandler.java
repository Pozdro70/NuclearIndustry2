package com.pozdro.nuclearindustry.tile;

import com.pozdro.nuclearindustry.tile.basicte.*;
import com.pozdro.nuclearindustry.tile.tankte.TankMachineGuiHandler;
import com.pozdro.nuclearindustry.tile.tankte.TankTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class ModGuiHandler implements IGuiHandler {

    @Nullable
    @Override
    public Object getServerGuiElement(
            int ID,
            EntityPlayer player,
            World world,
            int x,
            int y,
            int z) {

        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));

        if (te instanceof TankTileEntity) {
            TankTileEntity leacherTileEntity = (TankTileEntity) te;

            TankMachineGuiHandler<?> handler = leacherTileEntity.getTankGuiHandler();

            return handler.getServerGuiElement(
                    ID,player,world,x,y,z
            );
        }

        if (te instanceof BasicTileEntity) {
            BasicTileEntity tile = (BasicTileEntity) te;

            BasicMachineGuiHandler<?> handler = tile.getGuiHandler();

            return handler.getServerGuiElement(
                    ID, player, world, x, y, z
            );
        }

        if (te instanceof BasicChestEntity){
            BasicChestEntity tile = (BasicChestEntity) te;
            BasicChestGuiHandler<?> handler = tile.getGuiHandler();
            return handler.getServerGuiElement(
                    ID, player, world, x, y, z
            );
        }

        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(
            int ID,
            EntityPlayer player,
            World world,
            int x,
            int y,
            int z) {

        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));

        if (te instanceof TankTileEntity) {
            TankTileEntity tile = (TankTileEntity) te;

            TankMachineGuiHandler<?> handler = tile.getTankGuiHandler();

            return handler.getClientGuiElement(
                    ID, player, world, x, y, z
            );
        }

        if (te instanceof BasicTileEntity) {
            BasicTileEntity tile = (BasicTileEntity) te;

            BasicMachineGuiHandler<?> handler = tile.getGuiHandler();

            return handler.getClientGuiElement(
                    ID, player, world, x, y, z
            );
        }

        if(te instanceof BasicChestEntity) {
            BasicChestEntity tile = (BasicChestEntity) te;
            BasicChestGuiHandler<?> handler = tile.getGuiHandler();
            return handler.getClientGuiElement(ID, player, world, x, y, z);
        }

        return null;
    }
}