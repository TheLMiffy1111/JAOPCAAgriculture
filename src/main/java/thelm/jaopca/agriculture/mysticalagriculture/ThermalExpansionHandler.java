package thelm.jaopca.agriculture.mysticalagriculture;

import java.lang.reflect.Field;

import cofh.thermalexpansion.init.TEPlugins;
import cofh.thermalexpansion.plugins.PluginMysticalAgriculture;
import cofh.thermalexpansion.util.managers.machine.InsolatorManager;
import cofh.thermalexpansion.util.managers.machine.InsolatorManager.Type;
import net.minecraft.item.ItemStack;

public class ThermalExpansionHandler {

	public static void addInsolatorRecipes(ItemStack seeds, ItemStack essence) {
		int chanceBase = 100;
		int chanceRich = 110;
		int chanceFlux = 115;
		Field pluginField;
		try {
			pluginField = TEPlugins.class.getDeclaredField("pluginMysticalAgriculture");
			pluginField.setAccessible(true);
			PluginMysticalAgriculture plugin = (PluginMysticalAgriculture)pluginField.get(null);
			chanceBase = plugin.secondaryChanceBase;
			chanceRich = plugin.secondaryChanceRich;
			chanceFlux = plugin.secondaryChanceFlux;
		}
		catch(NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
		InsolatorManager.addDefaultRecipe(9600, 2400, seeds, essence, seeds, chanceBase, chanceRich, chanceFlux, Type.STANDARD);
	}
}
