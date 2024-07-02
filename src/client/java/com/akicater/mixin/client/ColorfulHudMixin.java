package com.akicater.mixin.client;

import com.akicater.CUIConfig;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

	@Shadow
	private static final Identifier ICONS = new Identifier("textures/gui/icons.png");

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

	public int getU(boolean halfHeart, boolean blinking) {
		int i;
		int j = halfHeart ? 1 : 0;
		int k = false && blinking ? 2 : 0;
		i = j + k;

		return 16 + (10 * 2) * 9;
	}

	@Inject(at = @At(value = "HEAD"), method = "drawHeart")
	private void color(DrawContext context, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {
		if (type == InGameHud.HeartType.NORMAL) {
			context.setShaderColor(r,g,b,1);
		}
	}

	@Inject(at = @At(value = "TAIL"), method = "drawHeart")
	private void uncolor(DrawContext context, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {
		context.setShaderColor(1,1,1,1);
		context.drawTexture(ICONS, x, y, 16 + (10 * 2) * 9, v, 9, 9);
	}
}