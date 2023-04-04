package io.mdt.ait.tardis.link;

import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.TARDISTravel;
import io.mdt.ait.tardis.door.TARDISDoor;
import io.mdt.ait.tardis.exterior.TARDISExterior;
import io.mdt.ait.tardis.interior.TARDISInterior;
import io.mdt.ait.tardis.state.TARDISStateManager;
import java.util.UUID;

/** Used for identifying objects that need to be linked to a TARDIS */
public interface TARDISLinkableStatic<T> {
    TARDISLink getLink(T t);

    default boolean isLinked(T t) {
        return this.getLink(t).isLinked();
    }

    default void link(T t, TARDIS tardis) {
        this.getLink(t).link(tardis);
    }

    default TARDIS getTARDIS(T t) {
        return this.getLink(t).getTARDIS();
    }

    default UUID getUUID(T t) {
        return this.getLink(t).getTARDIS().getUUID();
    }

    default TARDISDoor getDoor(T t) {
        return this.getLink(t).getTARDIS().getDoor();
    }

    default TARDISExterior getExterior(T t) {
        return this.getLink(t).getTARDIS().getExterior();
    }

    default TARDISInterior getInterior(T t) {
        return this.getLink(t).getTARDIS().getInterior();
    }

    default TARDISStateManager getStateManager(T t) {
        return this.getLink(t).getTARDIS().getStateManager();
    }

    default TARDISTravel getTravelManager(T t) {
        return this.getLink(t).getTARDIS().getTravelManager();
    }
}
