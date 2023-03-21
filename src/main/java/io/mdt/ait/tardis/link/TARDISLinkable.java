package io.mdt.ait.tardis.link;

import io.mdt.ait.tardis.TARDIS;
import io.mdt.ait.tardis.TARDISTravel;
import io.mdt.ait.tardis.door.TARDISDoor;
import io.mdt.ait.tardis.exterior.TARDISExterior;
import io.mdt.ait.tardis.interior.TARDISInterior;
import io.mdt.ait.tardis.state.TARDISStateManager;
import java.util.UUID;

/** Used for identifying objects that need to be linked to a TARDIS */
public interface TARDISLinkable {
    TARDISLink getLink();

    default boolean isLinked() {
        return this.getLink().isLinked();
    }

    default void link(TARDIS tardis) {
        this.getLink().link(tardis);

        System.out.println("------------------------");
        System.out.println("Myself: " + this);
        System.out.println("Linked with: " + tardis);
        System.out.println("------------------------");
    }

    default TARDIS getTARDIS() {
        return this.getLink().getTARDIS();
    }

    default UUID getUUID() {
        return this.getLink().getTARDIS().getUUID();
    }

    default TARDISDoor getDoor() {
        return this.getLink().getTARDIS().getDoor();
    }

    default TARDISExterior getExterior() {
        return this.getLink().getTARDIS().getExterior();
    }

    default TARDISInterior getInterior() {
        return this.getLink().getTARDIS().getInterior();
    }

    default TARDISStateManager getStateManager() {
        return this.getLink().getTARDIS().getStateManager();
    }

    default TARDISTravel getTravelManager() {
        return this.getLink().getTARDIS().getTravelManager();
    }
}
