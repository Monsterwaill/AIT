package io.mdt.ait.tardis.link.impl;

import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.TARDISManager;
import io.mdt.ait.tardis.link.TARDISLink;
import io.mdt.ait.tardis.link.TARDISLinkableStatic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public abstract class TARDISLinkableItem extends Item implements TARDISLinkableStatic<ItemStack> {

    public TARDISLinkableItem(Properties properties) {
        super(properties);
    }

    /**
     * Gets link for this {@link ItemStack}.
     */
    @Override
    public TARDISLink getLink(ItemStack stack) {
        TARDISLink link = new TARDISLink();
        CompoundNBT nbt = stack.getOrCreateTag();

        if (nbt.contains("tardis")) {
            link.link(TARDISManager.getInstance().findTARDIS(nbt.getUUID("tardis")));
        }

        return link;
    }

    @Override
    public void link(ItemStack stack, TARDIS tardis) {
        stack.getOrCreateTag().putUUID("tardis", tardis.getUUID());
    }
}
