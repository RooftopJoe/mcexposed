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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;

import rooftopjoe.mcexposed.Main;

@Mixin(Enchantment.class)
@Environment(EnvType.CLIENT)
public abstract class EnchantmentMixin {
	@Shadow
	public abstract int getMaxLevel();

	@Inject(at = @At("TAIL"), method = "Lnet/minecraft/enchantment/Enchantment;getName(I)Lnet/minecraft/text/Text;", locals = LocalCapture.CAPTURE_FAILSOFT)
	private void showMaxEnchantmentLevel(int level, CallbackInfoReturnable<Text> info, MutableText name) {
		if (Main.configManager.isShowMaxEnchantmentLevel() && getMaxLevel() > 1)
			name.append("/").append(Text.translatable("enchantment.level." + getMaxLevel()));
	}
}
