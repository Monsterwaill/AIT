package com.mdt.ait.common.items;

import io.mdt.ait.tardis.link.impl.TARDISLinkableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;

public class StattenheimRemoteItem extends TARDISLinkableItem {

    public StattenheimRemoteItem(Item.Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();

        return ActionResultType.sidedSuccess(world.isClientSide());
    }
}
