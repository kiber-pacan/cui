package com.akicater;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class CuiClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		AutoConfig.register(CUIConfig.class, Toml4jConfigSerializer::new);
	}
}