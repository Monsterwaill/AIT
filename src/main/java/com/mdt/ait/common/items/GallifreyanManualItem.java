package com.mdt.ait.common.items;

import net.minecraft.item.WritableBookItem;

public class GallifreyanManualItem extends WritableBookItem {

    public GallifreyanManualItem(Properties pProperties) {
        super(pProperties);
    }

    /*@Override
    public ActionResult<ItemStack> use(World pLevel, PlayerEntity pPlayer, Hand pHand) {
        ItemStack itemstack = pPlayer.getMainHandItem();
        Minecraft.getInstance().setScreen(new GallifreyanManualBookScreen(new ReadBookScreen.WrittenBookInfo(itemstack)));
        return super.use(pLevel, pPlayer, pHand);
    }*/
}
