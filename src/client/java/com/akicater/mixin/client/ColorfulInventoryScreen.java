package com.akicater.mixin.client;

import com.akicater.CUIConfig;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.akicater.CuiClient.hsvToRgb;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

@Mixin(InventoryScreen.class)
public class ColorfulInventoryScreen {

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

	@Unique
	float i = 0;
	@Unique
	float delta = 0;
	@Unique
	float last = 0;
	@Unique
	float current = 0;

	@Inject(at = @At(value = "HEAD"), method = "render")
	private void configColor(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (config.rainbow) {
			current = (float) glfwGetTime();
			delta = current - last;
			last = current;
			i += config.ranbowSpeed * delta;
			if (i >= 360) i = 0;
			Vec3d rgb = hsvToRgb(i/360,1.0f,1.0f);
			r = (float) rgb.x;
			g = (float) rgb.y;
			b = (float) rgb.z;
		} else {
			r = convert("" + config.color.charAt(1) + config.color.charAt(2));
			g = convert("" + config.color.charAt(3) + config.color.charAt(4));
			b = convert("" + config.color.charAt(5) + config.color.charAt(6));
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", shift = At.Shift.BEFORE), method = "drawBackground")
	private void color(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
		context.setShaderColor(r,g,b,1);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", shift = At.Shift.AFTER), method = "drawBackground")
	private void uncolor(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
		context.setShaderColor(1,1,1,1);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;drawTooltip(Lnet/minecraft/client/gui/DrawContext;IIII)V", shift = At.Shift.BEFORE), method = "render")
	private void color(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		context.setShaderColor(r,g,b,1);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;drawTooltip(Lnet/minecraft/client/gui/DrawContext;IIII)V", shift = At.Shift.AFTER), method = "render")
	private void uncolor(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		context.setShaderColor(1,1,1,1);
	}
}