package com.pozdro.nuclearindustry.tile.tankte;

import com.pozdro.nuclearindustry.tile.IHasInventory;
import com.pozdro.nuclearindustry.tile.IHasProgressAndEnergy;
import com.pozdro.nuclearindustry.tile.ISettableTank;
import com.pozdro.nuclearindustry.tile.basicte.TileSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class TankMachineGuiHandler<T extends TileEntity & IHasInventory & IHasProgressAndEnergy & ISettableTank> implements IGuiHandler {
    /*
    private static final Map<Integer, Map.Entry<Integer, Integer>> guiSlots = new HashMap<>();
    static {
        guiSlots.put(0, new AbstractMap.SimpleEntry<>(10, 77));
        guiSlots.put(1, new AbstractMap.SimpleEntry<>(20, 7));
    }
     */

    List<TileSlot> guiSlots;
    private final ResourceLocation guiTexture;
    private final Class<T> tile;
    private final String containerName;
    private final boolean drawArrowHorizontally;
    private final int arrowHightPx, arrowWidthPx;
    private final int arrowDrawX, arrowDrawY;
    private final int arrowSpriteX, arrowSpriteY;
    private final int guiWitdh, guiHeight;
    private final int playerInvYOffset;
    private final int energyBarPosX,energyBarPosY;
    private final int energyBarRenderHeight;


    private final Map<Integer, Map.Entry<Integer, Integer>> tankSize;      // <tankID, <height, width>>
    private final Map<Integer, Map.Entry<Integer, Integer>> tankPos;       // <tankID, <x, y>>


    public TankMachineGuiHandler(List<TileSlot> guiSlots, ResourceLocation guiTexture, Class<T> tile, String containerName,
                                 boolean drawArrowHorizontally, int arrowHightPx, int arrowWidthPx, int arrowDrawX, int arrowDrawY, int arrowSpriteX,
                                 int arrowSpriteY, int guiWitdh, int guiHeight, int playerInvYOffset, Map<Integer, Map.Entry<Integer, Integer>> tankSize,
                                 Map<Integer, Map.Entry<Integer, Integer>> tankPos, int energyBarPosX, int energyBarPosY, int energyBarRenderHeight){

        this.guiSlots = guiSlots;
        this.guiTexture = guiTexture;
        this.tile = tile;
        this.containerName = containerName;
        this.drawArrowHorizontally = drawArrowHorizontally;
        this.arrowHightPx = arrowHightPx;
        this.arrowWidthPx = arrowWidthPx;
        this.arrowDrawX = arrowDrawX;
        this.arrowDrawY = arrowDrawY;
        this.arrowSpriteX = arrowSpriteX;
        this.arrowSpriteY = arrowSpriteY;
        this.guiWitdh = guiWitdh;
        this.guiHeight = guiHeight;
        this.playerInvYOffset = playerInvYOffset;
        this.energyBarPosX = energyBarPosX;
        this.energyBarPosY = energyBarPosY;
        this.energyBarRenderHeight = energyBarRenderHeight;
        this.tankSize = tankSize;
        this.tankPos = tankPos;
    }


    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
        return tile.isInstance(te) ? new TankMachineContainer<T>(player.inventory, (T) tile.cast(te),guiSlots,playerInvYOffset) : null;

    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        return tile.isInstance(te) ? new TankMachineGui<T>(player.inventory,(T) tile.cast(te), guiSlots,
                guiTexture,containerName,
                guiWitdh,guiHeight,drawArrowHorizontally,arrowHightPx,arrowWidthPx,arrowDrawX,arrowDrawY,arrowSpriteX,arrowSpriteY,playerInvYOffset,
                tankSize,tankPos,energyBarPosX,energyBarPosY,energyBarRenderHeight) : null;
    }
}
