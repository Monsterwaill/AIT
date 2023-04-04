package com.mdt.ait.common.items;

import io.mdt.ait.tardis.key.KeyInteractable;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class TARDISKey extends Item {

    public TARDISKey(Properties properties) {
        super(properties);
    }

    private Optional<ActionResultType> useKeyOn(Object object, PlayerEntity player, ItemStack stack) {
        if (object instanceof KeyInteractable) {
            return Optional.of(((KeyInteractable) object).onKey(player, stack));
        }

        return Optional.empty();
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        return this.useKeyOn(world.getBlockEntity(context.getClickedPos()), player, stack)
                .orElseGet(() -> ActionResultType.sidedSuccess(world.isClientSide()));
    }

    @Override
    public void appendHoverText(
            ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
    }
}
