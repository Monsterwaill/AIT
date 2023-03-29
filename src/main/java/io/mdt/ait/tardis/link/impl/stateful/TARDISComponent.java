package io.mdt.ait.tardis.link.impl.stateful;

import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.link.impl.TARDISLinkableTileEntity;
import io.mdt.ait.tardis.state.TARDISComponentState;
import net.minecraft.tileentity.TileEntityType;

import java.util.function.Consumer;

public abstract class TARDISComponent<T extends TARDISComponentState<?>> extends TARDISLinkableTileEntity {

    private final Class<T> stateClass;

    public TARDISComponent(TileEntityType<?> type, Class<T> stateClass) {
        super(type);

        this.stateClass = stateClass;
    }

    @Override
    public void link(TARDIS tardis) {
        super.link(tardis);

        this.getStateManager().add(
                this.createState()
        );
    }

    public abstract TARDISComponentState<T> createState();

    public T getState() {
        return this.getStateManager()
                .get(this.stateClass);
    }

    public void updateState(Consumer<T> consumer) {
        this.getStateManager().update(this.stateClass, consumer);
    }
}
