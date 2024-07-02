package com.akicater;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cui implements ModInitializer {
	public static final String MODID = "cui";
    public static final Logger LOGGER = LoggerFactory.getLogger("cui");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
	}
}