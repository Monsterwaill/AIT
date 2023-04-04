package com.mdt.ait.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;

public class TestBlock extends Block {
    public TestBlock() {
        super(Properties.of(Material.STONE, MaterialColor.COLOR_GRAY)
                .harvestTool(ToolType.PICKAXE)
                .sound(SoundType.STONE));
    }
}
