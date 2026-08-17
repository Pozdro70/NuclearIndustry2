package com.pozdro.nuclearindustry.tile;

import com.pozdro.nuclearindustry.tile.ISettableTank;
import com.pozdro.nuclearindustry.tile.basicte.BasicMachineGuiHandler;
import com.pozdro.nuclearindustry.tile.basicte.BasicTileEntity;
import com.pozdro.nuclearindustry.tile.tankte.TankMachineGuiHandler;
import com.pozdro.nuclearindustry.tile.tiles.LeacherTileEntity;
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

        if (te instanceof BasicTileEntity) {
            BasicTileEntity tile = (BasicTileEntity) te;

            BasicMachineGuiHandler<?> handler = tile.getGuiHandler();

            return handler.getServerGuiElement(
                    ID, player, world, x, y, z
            );
        }

        // handle tank machines separately
        if (te instanceof LeacherTileEntity) {
            LeacherTileEntity leacherTileEntity = (LeacherTileEntity) te;

            TankMachineGuiHandler<?> handler = leacherTileEntity.getGuiHandler();

            return handler.getServerGuiElement(
                    ID,player,world,x,y,z
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

        if (te instanceof BasicTileEntity) {
            BasicTileEntity tile = (BasicTileEntity) te;

            BasicMachineGuiHandler<?> handler = tile.getGuiHandler();

            return handler.getClientGuiElement(
                    ID, player, world, x, y, z
            );
        }

        // handle tank machines separately
        if (te instanceof LeacherTileEntity) {
            LeacherTileEntity tile = (LeacherTileEntity) te;

            TankMachineGuiHandler<?> handler = tile.getGuiHandler();

            return handler.getClientGuiElement(
                    ID, player, world, x, y, z
            );
        }

        return null;
    }
}