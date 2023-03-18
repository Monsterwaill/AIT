package com.mdt.ait.common.items;

import io.mdt.ait.common.tiles.TARDISTileEntity;
import io.mdt.ait.tardis.link.impl.TARDISLinkableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StattenheimRemoteItem extends TARDISLinkableItem {

    public StattenheimRemoteItem(Item.Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        TileEntity tile = world.getBlockEntity(pos);
        if (tile instanceof TARDISTileEntity) {
            this.link(stack, ((TARDISTileEntity) tile).getTARDIS());
            return ActionResultType.SUCCESS;
        }

        this.getTravelManager(stack).to(pos);
        return ActionResultType.sidedSuccess(world.isClientSide());
    }
}
