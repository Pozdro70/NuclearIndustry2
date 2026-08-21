package com.pozdro.nuclearindustry.tile.basicte;

import com.pozdro.nuclearindustry.tile.IHasInventory;
import com.pozdro.nuclearindustry.tile.IHasProgressAndEnergy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BasicMachineGuiHandler<T extends TileEntity & IHasInventory & IHasProgressAndEnergy> implements IGuiHandler {
    /*
    private static final Map<Integer, Map.Entry<Integer, Integer>> guiSlots = new HashMap<>();
    static {
        guiSlots.put(0, new AbstractMap.SimpleEntry<>(10, 77));
        guiSlots.put(1, new AbstractMap.SimpleEntry<>(20, 7));
    }
     */

    private final List<TileSlot> guiSlots;

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
    private final int guiID;


    public BasicMachineGuiHandler(List<TileSlot> guiSlots, ResourceLocation guiTexture, Class<T> tile, String containerName,
                                  boolean drawArrowHorizontally, int arrowHightPx, int arrowWidthPx, int arrowDrawX, int arrowDrawY, int arrowSpriteX,
                                  int arrowSpriteY, int guiWitdh, int guiHeight, int playerInvYOffset,
                                  int energyBarPosX, int energyBarPosY, int energyBarRenderHeight, int guiID){

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
        this.guiID = guiID;
    }


    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID != guiID) {
            return null;
        }
        TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
        return tile.isInstance(te) ? new BasicMachineContainer<T>(player.inventory, (T) tile.cast(te),guiSlots,playerInvYOffset) : null;

    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID != guiID) {
            return null;
        }
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        return tile.isInstance(te) ? new BasicMachineGui<T>(player.inventory,(T) tile.cast(te), guiSlots,
                guiTexture,containerName,
                guiWitdh,guiHeight,drawArrowHorizontally,arrowHightPx,arrowWidthPx,arrowDrawX,arrowDrawY,arrowSpriteX,arrowSpriteY,playerInvYOffset,
                energyBarPosX,energyBarPosY,energyBarRenderHeight) : null;
    }

    //USED BY JEI

    public ResourceLocation getGuiTexture() {
        return guiTexture;
    }

    public int getGuiHeight() {
        return guiHeight;
    }

    public int getGuiWitdh() {
        return guiWitdh;
    }

    public String getContainerName() {
        return containerName;
    }

    public List<TileSlot> getGuiSlots() {
        return new ArrayList<TileSlot>(guiSlots);
    }

    public int getArrowSpriteY() {
        return arrowSpriteY;
    }

    public int getArrowSpriteX() {
        return arrowSpriteX;
    }

    public int getArrowDrawY() {
        return arrowDrawY;
    }

    public int getArrowDrawX() {
        return arrowDrawX;
    }

    public int getArrowWidthPx() {
        return arrowWidthPx;
    }

    public int getArrowHightPx() {
        return arrowHightPx;
    }
}
