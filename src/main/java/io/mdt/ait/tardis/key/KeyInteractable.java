package io.mdt.ait.tardis.key;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;

public interface KeyInteractable {
    ActionResultType onKey(PlayerEntity player, ItemStack stack);
}
