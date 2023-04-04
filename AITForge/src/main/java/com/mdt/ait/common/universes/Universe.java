package com.mdt.ait.common.universes;

import com.mdt.ait.core.init.enums.EnumDangerLevel;

public class Universe {

    public String name;
    public EnumDangerLevel dangerLevel;

    public Universe(String name, EnumDangerLevel dangerLevel) {
        this.name = name;
        this.dangerLevel = dangerLevel;
    }

    public String getName() {
        return this.name;
    }
}
