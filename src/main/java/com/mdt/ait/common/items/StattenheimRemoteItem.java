package com.mdt.ait.common.items;

import io.mdt.ait.common.tiles.TARDISTileEntity;
import io.mdt.ait.nbt.wrapped.AbsoluteBlockPos;
import io.mdt.ait.tardis.link.TARDISLink;
import io.mdt.ait.tardis.link.impl.TARDISLinkableItem;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class StattenheimRemoteItem extends TARDISLinkableItem {

    public StattenheimRemoteItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        if (!this.isLinked(stack)) {
            TileEntity tile = world.getBlockEntity(pos);
            if (tile instanceof TARDISTileEntity) {
                this.link(stack, ((TARDISTileEntity) tile).getTARDIS());

                return ActionResultType.SUCCESS;
            }
        }

        this.getTravelManager(stack).to(new AbsoluteBlockPos(world.dimension(), pos));
        return ActionResultType.SUCCESS;
    }

    // Hover text
    // * If unlinked, show an unlinked text
    // * If linked, show info on the linked TARDIS
    @Override
    public void appendHoverText(
            ItemStack pStack, @Nullable World pLevel, List<ITextComponent> pTooltip, ITooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        TARDISLink link = this.getLink(pStack);
        if (link.isLinked()) {
            UUID TARDISUUID =
                    link.getTARDIS().getUUID(); // it pisses me off that our coding style means this shit is all caps :/
            pTooltip.add(new TranslationTextComponent(("TARDIS: " + TARDISUUID.toString()))
                    .setStyle(Style.EMPTY.withItalic(true).withColor(TextFormatting.GREEN)));
        } else {
            pTooltip.add(new TranslationTextComponent("Right-Click a TARDIS to link.")
                    .setStyle(Style.EMPTY.withItalic(true).withColor(TextFormatting.RED)));
        }
    }
}
