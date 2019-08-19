package thelm.jaopca.agriculture.mysticalagriculture;

import cofh.thermalexpansion.init.TEPlugins;
import cofh.thermalexpansion.util.managers.machine.InsolatorManager;
import cofh.thermalexpansion.util.managers.machine.InsolatorManager.Type;
import net.minecraft.item.ItemStack;

public class ThermalExpansionHandler {

	public static void addInsolatorRecipes(ItemStack seeds, ItemStack essence) {
		InsolatorManager.addDefaultRecipe(9600, 2400, seeds, essence, seeds, TEPlugins.pluginMysticalAgriculture.secondaryChanceBase,
				TEPlugins.pluginMysticalAgriculture.secondaryChanceRich, TEPlugins.pluginMysticalAgriculture.secondaryChanceFlux, Type.STANDARD);
	}
}
