package io.mdt.ait.tardis.interior;

import io.mdt.ait.tardis.interior.impl.AirInteriorSchema;
import io.mdt.ait.tardis.interior.impl.DevInteriorSchema;

import java.util.HashMap;
import java.util.Map;

public class TARDISInteriors {

    private static final Map<String, TARDISInteriorSchema> interiors = new HashMap<>();

    /**
     * All default interiors are registered here!
     */
    public static void init() {
        TARDISInteriors.register(new DevInteriorSchema());
        TARDISInteriors.register(new AirInteriorSchema());
    }

    public static void register(TARDISInteriorSchema interior) {
        interiors.put(interior.getId(), interior);
    }

    public static TARDISInteriorSchema get(String id) {
        return interiors.get(id);
    }
}
