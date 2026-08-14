package com.pozdro.nuclearindustry.block;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.init.Blocks;

public class ModMultiblockPatterns {
    public static final BlockPattern LEACHER_PLANT = FactoryBlockPattern.start()
            .aisle("CHC","HHH","CHC")
            .aisle(" HH","HHH","HH ")
            .where(' ', matchBlock(Blocks.AIR))
            .where('H', matchBlock(ModBlocks.MACHINE_HULL))
            .where('C', matchBlock(ModBlocks.MACHINE_CASING))
            .build();

    private static Predicate<BlockWorldState> matchBlock(Block block) {
        BlockStateMatcher matcher = BlockStateMatcher.forBlock(block);
        return input -> input != null && matcher.apply(input.getBlockState());
    }
}
