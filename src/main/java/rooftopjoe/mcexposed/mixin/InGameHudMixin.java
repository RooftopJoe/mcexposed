/*
 * Copyright 2022 Bogdan Barbu
 *
 * This file is part of MC Exposed.
 *
 * MC Exposed is free software: you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * MC Exposed is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * MC Exposed. If not, see <https://www.gnu.org/licenses/>.
 */

package rooftopjoe.mcexposed.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.util.Formatting;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;

import java.util.Collection;

import com.google.common.collect.Ordering;

import rooftopjoe.mcexposed.Main;

@Mixin(InGameHud.class)
@Environment(EnvType.CLIENT)
public abstract class InGameHudMixin extends DrawableHelper {
	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay(Lnet/minecraft/client/util/math/MatrixStack;)V")
	private void showStatusEffectInfo(MatrixStack matrices, CallbackInfo callback) {
		Collection<StatusEffectInstance> effects = client.player.getStatusEffects();
		int beneficial = 0, unbeneficial = 0;

		if (effects.isEmpty())
			return;

		for (StatusEffectInstance instance : Ordering.natural().reverse().sortedCopy(effects)) {
			int x = client.getWindow().getScaledWidth(), y = client.isDemo() ? 16 : 1;

			if (!instance.shouldShowIcon())
				continue;

			if (instance.getEffectType().isBeneficial()) {
				beneficial++;
				x -= 25 * beneficial;
			} else {
				unbeneficial++;
				x -= 25 * unbeneficial;
				y += 26;
			}

			if (Main.configManager.isShowStatusEffectAmplifier()) {
				int amplifier = instance.getAmplifier();
				String amplifierText = amplifier <= 5 ? I18n.translate("potion.potency." + amplifier) : String.valueOf(amplifier);

				drawStringWithShadow(matrices, client.textRenderer, amplifierText, x + 12 - client.textRenderer.getWidth(amplifierText) / 2, y + 3, Formatting.WHITE.getColorValue());				
			}

			if (Main.configManager.isShowStatusEffectDuration()) {
				String durationText = StatusEffectUtil.durationToString(instance, 1);

				drawStringWithShadow(matrices, client.textRenderer, durationText, x + 12 - client.textRenderer.getWidth(durationText) / 2, y + 14, Formatting.GRAY.getColorValue());
			}
		}
	}
}
