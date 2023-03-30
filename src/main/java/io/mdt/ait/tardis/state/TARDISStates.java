package io.mdt.ait.tardis.state;

import com.mdt.ait.common.tileentities.state.ExteriorFacingControlState;
import com.mdt.ait.common.tileentities.state.TARDISLeverState;
import java.util.HashMap;
import java.util.Map;

public class TARDISStates {

    private static final Map<String, TARDISComponentState<?>> states = new HashMap<>();

    /** All default interiors are registered here! */
    public static void init() {
        TARDISStates.register(new ExteriorFacingControlState());
        TARDISStates.register(new TARDISLeverState());
    }

    public static void register(TARDISComponentState<?> state) {
        states.put(state.getId(), state);
    }

    public static TARDISComponentState<?> get(String id) {
        return states.get(id);
    }
}
