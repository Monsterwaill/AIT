package com.mdt.ait.common.blocks;

import net.minecraft.block.BarrierBlock;
import net.minecraft.block.material.Material;

public class InvisBlock extends BarrierBlock {

    public InvisBlock() {
        super(Properties.of(Material.HEAVY_METAL).strength(15.0f).noOcclusion().instabreak());
    }
}
