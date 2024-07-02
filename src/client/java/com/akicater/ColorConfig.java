package com.akicater;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "modid")
public class ColorConfig implements ConfigData {
    int R = 0;
    int G = 0;
    int B = 0;

    @ConfigEntry.Gui.CollapsibleObject
    InnerStuff stuff = new InnerStuff();

    @ConfigEntry.Gui.Excluded
    InnerStuff invisibleStuff = new InnerStuff();

    static class InnerStuff {
        int R = 0;
        int G = 0;
        int B = 0;
    }
}