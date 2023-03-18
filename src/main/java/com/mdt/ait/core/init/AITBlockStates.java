package com.mdt.ait.core.init;

import com.mdt.ait.core.init.enums.EnumConsoleType;
import com.mdt.ait.core.init.enums.EnumExteriorType;
import net.minecraft.state.EnumProperty;

public class AITBlockStates {
    public static final EnumProperty<EnumExteriorType> TARDIS_EXTERIOR =
            EnumProperty.create("tardis_exterior", EnumExteriorType.class);

    public static final EnumProperty<EnumConsoleType> TARDIS_CONSOLE =
            EnumProperty.create("tardis_console", EnumConsoleType.class);
}
