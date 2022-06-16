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
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.FoodComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.text.Text;
import net.minecraft.block.ComposterBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.util.Formatting;

import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;

import javax.annotation.Nullable;

import java.util.List;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.math.RoundingMode;
import java.util.ArrayList;

import rooftopjoe.mcexposed.Main;

@Mixin(ItemStack.class)
@Environment(EnvType.CLIENT)
public abstract class ItemStackMixin {
	@Shadow
	public abstract Item getItem();

	@Inject(at = @At("RETURN"), method = "Lnet/minecraft/item/ItemStack;getTooltip(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/item/TooltipContext;)Ljava/util/List;",
	        locals = LocalCapture.CAPTURE_FAILSOFT)
	private void onGetTooltip(@Nullable PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> ci, List<Text> list) {
		NumberFormat oneDecimal = DecimalFormat.getInstance();
		final Item item = getItem();
		int topLine = Math.min(1, list.size());
		int botLine = Math.max(1, list.size() - (context.isAdvanced() ? (((ItemStack)(Object)this).isDamaged() ? 1 : 0) + (((ItemStack)(Object)this).hasNbt() ? 1 : 0) + 1 : 0));

		if (player == null)
			return;

		oneDecimal.setMinimumFractionDigits(0);
		oneDecimal.setMaximumFractionDigits(1);
		oneDecimal.setRoundingMode(RoundingMode.HALF_UP);

		if (Main.configManager.isShowBlockHardness() && item instanceof BlockItem) {
			list.add(topLine, Text.translatable("tooltip.mcexposed.hardness")
				.append(": " + oneDecimal.format(((BlockItem)item).getBlock().getHardness()))
				.formatted(Formatting.GRAY));
			botLine++;
		}

		if (Main.configManager.isShowCompostingChance()) {
			final float chance = ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.getFloat(item);
			
			if (chance > 0) {
				list.add(topLine, Text.translatable("tooltip.mcexposed.compostingchance")
					.append(": " + oneDecimal.format(100 * chance) + "%")
					.formatted(Formatting.GRAY));
				botLine++;
			}
		}

		if (Main.configManager.isShowMiningSpeed() && item instanceof ToolItem) {
			final float multiplier = ((ToolItem)item).getMaterial().getMiningSpeedMultiplier();
			final int level = EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, (ItemStack)(Object)this);
			double speed = multiplier + Math.pow(level, 2) + (level > 0 ? 1 : 0);

			if (StatusEffectUtil.hasHaste(player))
				speed *= 0.2 * (StatusEffectUtil.getHasteAmplifier(player) + 1) + 1;

			if (player.hasStatusEffect(StatusEffects.MINING_FATIGUE))
				speed *= Math.pow(0.3, Math.min(player.getStatusEffect(StatusEffects.MINING_FATIGUE).getAmplifier() + 1, 4));

			if (player.isSubmergedInWater() && !EnchantmentHelper.hasAquaAffinity(player))
				speed /= 5;
			
			if (!player.isOnGround())
				speed /= 5;

			list.add(botLine, Text.literal(" " + oneDecimal.format(speed) + "x ")
			                      .append(Text.translatable("tooltip.mcexposed.miningspeed"))
			                      .formatted(Formatting.DARK_GREEN));
			botLine++;
		}

		if (Main.configManager.isShowFoodStats() && ((ItemStack)(Object)this).isFood()) {
			final List<Text> lines = new ArrayList<>();
			final FoodComponent component = item.getFoodComponent();
			final int hunger = component.getHunger();
			final float saturation = 2 * component.getSaturationModifier() * hunger;

			lines.add(Text.literal(""));
			lines.add(Text.translatable("tooltip.mcexposed.foodstats").append(":").formatted(Formatting.GRAY));
			lines.add(Text.translatable("attribute.modifier.plus.0", hunger, Text.translatable("tooltip.mcexposed.foodstats.hunger")).formatted(Formatting.DARK_GREEN));
			lines.add(Text.translatable("attribute.modifier.plus.0", oneDecimal.format(saturation), Text.translatable("tooltip.mcexposed.foodstats.saturation")).formatted(Formatting.DARK_GREEN));
			list.addAll(botLine, lines);
			botLine += 4;
		}
	}
}
