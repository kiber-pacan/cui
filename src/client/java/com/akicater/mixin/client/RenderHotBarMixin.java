package com.akicater.mixin.client;

import com.akicater.ColorConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class RenderHotBarMixin {

	@Unique
	ColorConfig config = AutoConfig.getConfigHolder(ColorConfig.class).getConfig();

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", shift = At.Shift.AFTER), method = "renderHotbar")
	private void color(float tickDelta, DrawContext context, CallbackInfo ci) {
		context.setShaderColor(135.0f / 255, 135.0f / 255, 255.0f / 255,1);
	}
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", shift = At.Shift.BEFORE), method = "renderHotbar")
	private void uncolor(float tickDelta, DrawContext context, CallbackInfo ci) {
		context.setShaderColor(1,1,1,1);
	}
}