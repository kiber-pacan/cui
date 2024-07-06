package com.akicater;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Unique;

public class CuiClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		AutoConfig.register(CUIConfig.class, Toml4jConfigSerializer::new);
	}

	@Unique
    public static Vec3d hsvToRgb(float hue, float saturation, float value) {
		int h = (int)(hue * 6);
		float f = hue * 6 - h;
		float p = value * (1 - saturation);
		float q = value * (1 - f * saturation);
		float t = value * (1 - (1 - f) * saturation);

		return switch (h) {
			case 0 -> new Vec3d(value, t, p);
			case 1 -> new Vec3d(q, value, p);
			case 2 -> new Vec3d(p, value, t);
			case 3 -> new Vec3d(p, q, value);
			case 4 -> new Vec3d(t, p, value);
			case 5 -> new Vec3d(value, p, q);
			default -> throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
		};
	}
}