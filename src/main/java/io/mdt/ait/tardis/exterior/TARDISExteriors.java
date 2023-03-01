package io.mdt.ait.tardis.exterior;

import io.mdt.ait.tardis.exterior.impl.BasicBoxExteriorSchema;

import java.util.HashMap;
import java.util.Map;

public class TARDISExteriors {

    private static final Map<String, TARDISExteriorSchema> exteriors = new HashMap<>();

    /**
     * All default exteriors are registered here!
     */
    public static void init() {
        TARDISExteriors.register(new BasicBoxExteriorSchema());
    }

    public static void register(TARDISExteriorSchema exterior) {
        exteriors.put(exterior.getId(), exterior);
    }

    public static TARDISExteriorSchema get(String id) {
        return exteriors.get(id);
    }
}
