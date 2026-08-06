package com.pozdro.nuclearindustry.block.custom;

import com.pozdro.nuclearindustry.NuclearIndustry;
import com.pozdro.nuclearindustry.block.tile.IHasInventory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class BasicMachineBlock extends Block {

    public static final PropertyBool LIT = PropertyBool.create("lit");
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private final Supplier<TileEntity> te;
    private final int guiID;

    public BasicMachineBlock(Material blockMaterialIn, Supplier<TileEntity> te,int guiID) {
        super(blockMaterialIn);

        this.setDefaultState(this.getBlockState().getBaseState()
                .withProperty(LIT,false)
                .withProperty(FACING, EnumFacing.NORTH)
        );

        this.te = te;
        this.guiID = guiID;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this,LIT,FACING);
    }


    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta & 3); // lowest 2 bits = 0-3 -> 4 directions
        boolean lit = (meta & 4) != 0;                          // 3rd bit = lit flag
        return this.getDefaultState().withProperty(FACING, facing).withProperty(LIT, lit);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(FACING).getHorizontalIndex(); // 0-3
        if (state.getValue(LIT)) meta |= 4;
        return meta;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing,
                                            float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {

        return this.getDefaultState()
                .withProperty(FACING, placer.getHorizontalFacing().getOpposite())
                .withProperty(LIT,false);
    }


    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(LIT) ? 12 : 0;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return true;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return true;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return te.get();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
                                    EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            player.openGui(NuclearIndustry.instance, guiID, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }


    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IHasInventory) {
            ((IHasInventory) te).dropInventory();
        }
        super.breakBlock(world, pos, state);
    }
}
