package com.pozdro.nuclearindustry.tile.chestte;

import com.pozdro.nuclearindustry.tile.IHasInventory;
import com.pozdro.nuclearindustry.tile.basicte.TileSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;
import java.util.List;

public class BasicChestGuiHandler<T extends TileEntity & IHasInventory> implements IGuiHandler {

    List<TileSlot> guiSlots;
    private final ResourceLocation guiTexture;
    private final Class<T> tile;
    private final String containerName;
    private final int guiWidth, guiHeight;
    private final int playerInvYOffset;
    private final int guiID;


    public BasicChestGuiHandler(List<TileSlot> guiSlots, ResourceLocation guiTexture, Class<T> tile, String containerName,
                                int guiWidth, int guiHeight, int playerInvYOffset, int guiID){

        this.guiSlots = guiSlots;
        this.guiTexture = guiTexture;
        this.tile = tile;
        this.containerName = containerName;
        this.guiWidth = guiWidth;
        this.guiHeight = guiHeight;
        this.playerInvYOffset = playerInvYOffset;
        this.guiID = guiID;
    }


    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID != guiID) {
            return null;
        }
        TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
        return tile.isInstance(te) ? new BasicChestContainer<T>(player.inventory, (T) tile.cast(te),guiSlots,playerInvYOffset) : null;

    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID != guiID) {
            return null;
        }
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        return tile.isInstance(te) ? new BasicChestGui<T>(player.inventory,
                (T) tile.cast(te),
                guiSlots,
                guiTexture,
                containerName,
                guiWidth,
                guiHeight,
                playerInvYOffset) : null;
    }
}
