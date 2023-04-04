package com.mdt.ait.common.tileentities;

import com.mdt.ait.common.tileentities.state.TARDISLeverState;
import com.mdt.ait.core.init.AITTiles;
import io.mdt.ait.tardis.TARDISTravel;
import io.mdt.ait.tardis.link.impl.stateful.TARDISComponent;
import io.mdt.ait.tardis.state.TARDISComponentState;
import io.mdt.ait.util.Util;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class TARDISLeverControlTile extends TARDISComponent<TARDISLeverState> implements ITickableTileEntity {

    private float leverPosition = 0;

    public TARDISLeverControlTile() {
        super(AITTiles.TARDIS_LEVER_TILE_ENTITY_TYPE.get(), TARDISLeverState.class);
    }

    public ActionResultType useOn(World world, PlayerEntity player, Hand hand) {
        if (world.isClientSide() || !this.isLinked() || hand != Hand.MAIN_HAND) return ActionResultType.FAIL;

        this.updateState(state -> {
            TARDISTravel.Result result =
                    this.getTravelManager().to(this.getTARDIS().getPosition());

            if (result != TARDISTravel.Result.SUCCESS)
                Util.sendMessage(
                        player,
                        new TranslationTextComponent("TARDIS has not finished its journey!")
                                .setStyle(Style.EMPTY.withColor(TextFormatting.DARK_AQUA)));

            state.setLeverState(state.getLeverState().next());
            this.sync();
        });

        return ActionResultType.SUCCESS;
    }

    public float getLeverPosition() {
        return this.leverPosition;
    }

    @Override
    public void tick() {
        if (!this.isLinked()) return;

        this.updateState(state -> {
            if (state.getLeverState() == TARDISLeverState.State.INACTIVE) {
                if (this.leverPosition > 0f) {
                    this.leverPosition -= 15.0f;
                } else {
                    this.leverPosition = 0f;
                }
            }
            if (state.getLeverState() == TARDISLeverState.State.ACTIVE) {
                if (this.leverPosition < 30f) {
                    this.leverPosition += 5.0f;
                } else {
                    this.leverPosition = 30f;
                }
            }
        });
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(this.getBlockPos()).inflate(10, 10, 10);
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return this.save(new CompoundNBT());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getBlockPos(), 0, save(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        this.load(this.getBlockState(), packet.getTag());
    }

    @Override
    public TARDISComponentState<TARDISLeverState> createState() {
        return new TARDISLeverState();
    }
}
