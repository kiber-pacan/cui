package com.akicater.mixin.client;

import com.akicater.CUIConfig;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
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

	@Unique
	float i = 0;
	@Unique
	float delta = 0;
	@Unique
	float last = 0;
	@Unique
	float current = 0;

	@Shadow
	private static final Identifier ICONS = new Identifier("textures/gui/icons.png");

	@Inject(at = @At(value = "HEAD"), method = "render")
	private void color(DrawContext context, float tickDelta, CallbackInfo ci) {
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

	@Inject(at = @At(value = "HEAD"), method = "drawHeart")
	private void color(DrawContext context, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {
		if (type == InGameHud.HeartType.NORMAL) {
			context.setShaderColor(r,g,b,1);
		}
	}

	@Inject(at = @At(value = "TAIL"), method = "drawHeart")
	private void uncolor(DrawContext context, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {
		context.setShaderColor(1,1,1,1);
		if (type == InGameHud.HeartType.NORMAL) {
			context.drawTexture(ICONS, x, y, 16 + (10 * 2) * 9, v, 9, 9);
		}
	}

	@Shadow
	public TextRenderer getTextRenderer() {
		return null;
	}

	@Shadow
	private int scaledWidth;

	@Shadow
	private int scaledHeight;

	@Inject(at = @At(value = "TAIL"), method = "renderExperienceBar")
	private void text(DrawContext context, int x, CallbackInfo ci) {
		if (MinecraftClient.getInstance().player.experienceLevel > 0) {
			MinecraftClient.getInstance().getProfiler().push("expLevel");
			String string = "" + MinecraftClient.getInstance().player.experienceLevel;
			context.setShaderColor(r,g,b,1);
			context.drawText(
					getTextRenderer(),
					string,
					(this.scaledWidth - this.getTextRenderer().getWidth(string)) / 2,
					this.scaledHeight - 31 - 4,
					16777215,
					false);
			context.setShaderColor(1,1,1,1);
			MinecraftClient.getInstance().getProfiler().pop();
		}
	}
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", shift = At.Shift.BEFORE), method = "renderExperienceBar")
	private void color(DrawContext context, int x, CallbackInfo ci) {
		context.setShaderColor(r, g, b, 1);
	}
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", shift = At.Shift.AFTER), method = "renderExperienceBar")
	private void uncolor(DrawContext context, int x, CallbackInfo ci) {
		context.setShaderColor(1, 1, 1, 1);
	}
}