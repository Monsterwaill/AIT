package com.mdt.ait.common.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

public class LaserShotEntity extends AbstractArrowEntity {

    public LaserShotEntity(EntityType<? extends AbstractArrowEntity> p_i48546_1_, World p_i48546_2_) {
        super(p_i48546_1_, p_i48546_2_);
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }
}
