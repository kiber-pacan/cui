package com.akicater;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import static com.akicater.Cui.MODID;
import static net.minecraft.text.Text.literal;
import static net.minecraft.text.Text.translatable;

@Config(name = MODID)
public class CUIConfig implements ConfigData {

    @ConfigEntry.ColorPicker
    public String color = "#00ff11";
    public boolean rainbow = false;
    public int rainbowSpeed = 20;

}