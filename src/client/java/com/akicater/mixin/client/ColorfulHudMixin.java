package com.akicater.mixin.client;

import com.akicater.CUIConfig;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class ColorfulHudMixin {

	@Unique
	CUIConfig config = AutoConfig.getConfigHolder(CUIConfig.class).getConfig();

	@Unique
	float convert(String hex) {
		return (float) Integer.parseInt(hex, 16) / 255;
	}

	@Unique
	float r;
	@Unique
	float g;
	@Unique
	float b;

	@Inject(at = @At(value = "HEAD"), method = "render")
	private void color(DrawContext context, float tickDelta, CallbackInfo ci) {
		r = convert("" + config.color.charAt(1) + config.color.charAt(2));
		g = convert("" + config.color.charAt(3) + config.color.charAt(4));
		b = convert("" + config.color.charAt(5) + config.color.charAt(6));
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", shift = At.Shift.AFTER), method = "renderHotbar")
	private void color(float tickDelta, DrawContext context, CallbackInfo ci) {
		context.setShaderColor(r, g, b, 1);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", shift = At.Shift.BEFORE), method = "renderHotbar")
	private void uncolor(float tickDelta, DrawContext context, CallbackInfo ci) {
		context.setShaderColor(1,1,1,1);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;blendFuncSeparate(Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;Lcom/mojang/blaze3d/platform/GlStateManager$SrcFactor;Lcom/mojang/blaze3d/platform/GlStateManager$DstFactor;)V", shift = At.Shift.AFTER), method = "renderCrosshair")
	private void color(DrawContext context, CallbackInfo ci) {
		RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_COLOR, GlStateManager.DstFactor.SRC_ALPHA, GlStateManager.SrcFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.DstFactor.ZERO);
		context.setShaderColor(r, g, b, 1);
	}

	@Inject(at = @At(value = "TAIL"), method = "renderCrosshair")
	private void uncolor(DrawContext context, CallbackInfo ci) {
		context.setShaderColor(1,1,1,1);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I", shift = At.Shift.AFTER), method = "renderHealthBar")
	private void color(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
		InGameHud.HeartType heartType = InGameHud.HeartType.fromPlayerState(player);
		if (heartType == InGameHud.HeartType.NORMAL) {
			context.setShaderColor(r,g,b,1);
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "dra", shift = At.Shift.AFTER), method = "renderHealthBar")
	private void uncolor(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
		context.setShaderColor(1,1,1,1);
	}
}