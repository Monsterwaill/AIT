package com.mdt.ait.common.tileentities;

import com.mdt.ait.common.tileentities.state.TARDISLeverState;
import com.mdt.ait.core.init.AITTiles;
import io.mdt.ait.tardis.TARDISTravel;
import io.mdt.ait.tardis.link.impl.stateful.TARDISComponent;
import io.mdt.ait.tardis.state.TARDISComponentState;
import java.util.UUID;
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

    public float leverPosition = 0;

    public TARDISLeverControlTile() {
        super(AITTiles.TARDIS_LEVER_TILE_ENTITY_TYPE.get(), TARDISLeverState.class);
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
        return new AxisAlignedBB(worldPosition).inflate(10, 10, 10);
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        return save(new CompoundNBT());
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, 0, save(new CompoundNBT()));
    }

    public ActionResultType useOn(World world, PlayerEntity playerEntity, Hand hand) {
        if (world.isClientSide()) return ActionResultType.FAIL;

        if (!this.isLinked()) return ActionResultType.FAIL;

        if (hand != Hand.MAIN_HAND) return ActionResultType.FAIL;

        this.updateState(state -> {
            state.setLeverState(state.getLeverState().next());

            TARDISTravel.Result result =
                    this.getTravelManager().to(this.getTARDIS().getPosition());
            if (result != TARDISTravel.Result.SUCCESS)
                playerEntity.sendMessage(
                        new TranslationTextComponent("TARDIS has not finished its journey!")
                                .setStyle(Style.EMPTY.withColor(TextFormatting.DARK_AQUA)),
                        UUID.randomUUID());

            state.setLeverState(state.getLeverState().next());
            this.sync();
        });

        return ActionResultType.SUCCESS;
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
