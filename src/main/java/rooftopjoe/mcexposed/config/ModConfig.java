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

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.autoconfig.ConfigData;

@Config(name = "mcexposed")
public class ModConfig extends PartitioningSerializer.GlobalData {
	@ConfigEntry.Category("hud")
	@ConfigEntry.Gui.TransitiveObject
	HudConfig hud = new HudConfig();

	@ConfigEntry.Category("tooltip")
	@ConfigEntry.Gui.TransitiveObject
	TooltipConfig tooltip = new TooltipConfig();
}

@Config(name = "hud")
class HudConfig implements ConfigData {
	@ConfigEntry.Gui.Tooltip boolean showStatusEffectAmplifier = true;
	@ConfigEntry.Gui.Tooltip boolean showStatusEffectDuration = true;
}

@Config(name = "tooltip")
class TooltipConfig implements ConfigData {
	@ConfigEntry.Gui.Tooltip boolean showBlockHardness = true;
	@ConfigEntry.Gui.Tooltip boolean showMaxEnchantmentLevel = true;
	@ConfigEntry.Gui.Tooltip boolean showMiningSpeed = true;
}
