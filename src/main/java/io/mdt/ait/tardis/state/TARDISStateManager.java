package io.mdt.ait.tardis.state;

import io.mdt.ait.nbt.NBTSerializeable;
import io.mdt.ait.nbt.NBTUnserializeableLate;
import io.mdt.ait.tardis.link.impl.TARDISLinkableBasic;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

public class TARDISStateManager extends TARDISLinkableBasic {

    private final List<TARDISComponentState<?>> states = new ArrayList<>();

    /**
     * Registers a state.
     *
     * @param state state you want to register.
     * @param <T> type of the state.
     */
    public <T extends TARDISComponentState<?>> void add(T state) {
        for (TARDISComponentState<?> oldState : this.states) {
            if (oldState.equals(state)) {
                return;
            }
        }

        this.states.add(state);
    }

    public <T extends TARDISComponentState<?>> void set(int index, T state) {
        this.states.set(index, state);
    }

    /**
     * This is a better way of updating your state. You can fit your entire update logic in the {@link Consumer} and not worry about performance!
     *
     * @param type your state's {@link Class}. Used to find the state among other states.
     * @param consumer the consumer that accepts {@link T} state.
     * @param <T> state you are searching for.
     */
    public <T extends TARDISComponentState<?>> void update(Class<T> type, Consumer<T> consumer) {
        TARDISComponentState<?> state = this.get(type);

        if (state != null) {
            consumer.accept(this.get(type));
        }
    }

    /**
     * You would probably want to use {@link #update(Class, Consumer)} instead, as it can handle your flow more efficiently.
     *
     * @param type your state's {@link Class}. Used to find the state among other states.
     * @return state with type of {@link T}.
     * @param <T> state you are searching for.
     */
    @Nullable @SuppressWarnings("unchecked")
    public <T extends TARDISComponentState<?>> T get(Class<T> type) {
        for (TARDISComponentState<?> state : this.states) {
            if (type.isInstance(state)) {
                return (T) state;
            }
        }

        throw new IllegalArgumentException(
                "State of this type does not exist or not registered for this state manager!");
    }

    public static class Serializer
            implements NBTSerializeable<TARDISStateManager>, NBTUnserializeableLate<TARDISStateManager> {
        @Override
        public void serialize(CompoundNBT nbt, TARDISStateManager manager) {
            ListNBT states = new ListNBT();

            for (TARDISComponentState<?> state : manager.states) {
                //noinspection deprecation
                states.add(state.getSerializer().serialize(state));
            }

            nbt.put("states", states);
        }

        @Override
        public void unserialize(CompoundNBT nbt, TARDISStateManager manager) {
            ListNBT states = nbt.getList("states", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < manager.states.size(); i++) {
                TARDISComponentState<?> state = manager.states.get(i);

                for (INBT inbt : states) {
                    CompoundNBT stateNBT = ((CompoundNBT) inbt);

                    if (state.getId().equals(stateNBT.getString("id"))) {
                        manager.set(i, state.getSerializer().unserialize(stateNBT));
                        break;
                    }
                }
            }
        }
    }
}
