package com.akicater;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.text.TranslatableTextContent;

public class CuiClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		AutoConfig.register(ColorConfig.class, Toml4jConfigSerializer::new);
	}
}