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

package rooftopjoe.mcexposed.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

public class ConfigManager {
	ModConfig config;

	public ConfigManager() {
		AutoConfig.register(ModConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));
		config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
	}

	public boolean isShowItemCounts() { return config.hud.showItemCounts; }
	public boolean isShowStatusEffectAmplifier() { return config.hud.showStatusEffectAmplifier; }
	public boolean isShowStatusEffectDuration() { return config.hud.showStatusEffectDuration; }

	public boolean isShowMaxEnchantmentLevel() { return config.tooltip.showMaxEnchantmentLevel; }
	public boolean isShowBlastResistance() { return config.tooltip.showBlastResistance; }
	public boolean isShowCompostingChance() { return config.tooltip.showCompostingChance; }
	public boolean isShowBlockHardness() { return config.tooltip.showBlockHardness; }
	public boolean isShowLuminance() { return config.tooltip.showLuminance; }
	public boolean isShowMiningSpeed() { return config.tooltip.showMiningSpeed; }
	public boolean isShowFoodStats() { return config.tooltip.showFoodStats; }
}
