package com.akicater;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.fabricmc.api.ModInitializer;

import net.minecraft.text.TranslatableTextContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cui implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("cui");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
	}
}