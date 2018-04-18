package thelm.jaopca.agriculture.mysticalagriculture;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraftforge.common.config.Configuration;
import thelm.jaopca.agriculture.agricraft.AgriPlantProperties;
import thelm.jaopca.agriculture.agricraft.AgriPlantProperties.Product;
import thelm.jaopca.agriculture.agricraft.EnumRenderType;
import thelm.jaopca.agriculture.agricraft.ModuleAgriCraft;
import thelm.jaopca.api.EnumOreType;
import thelm.jaopca.api.IItemRequest;
import thelm.jaopca.api.IOreEntry;
import thelm.jaopca.api.ItemEntry;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.ModuleBase;
import thelm.jaopca.api.utils.Utils;

public class ModuleMysticalAgriCraft extends ModuleBase {

	public static final HashMap<IOreEntry, Double> GROWTH_CHANCES = Maps.<IOreEntry, Double>newHashMap();
	public static final HashMap<IOreEntry, Double> GROWTH_BONUSES = Maps.<IOreEntry, Double>newHashMap();
	
	public static final AgriPlantProperties MYSTICAL_PLANT_PROPERTIES = new AgriPlantProperties().
			setSeedFormatFuncs(type->"mysticalSeeds%s").
			setProducts(new Product().
					setFormatFunc(type->"essence%s").
					setMinFunc(entry->1).
					setRangeFunc(entry->4).
					setChanceFunc(entry->0.9D)).
			setRenderType(EnumRenderType.CROSS).
			setAgriPlantClass(AgriPlantCropBase.class);

	public static final ItemEntry MYSTICAL_PLANT_ENTRY = new ItemEntry(ModuleAgriCraft.AGRI_PLANT, "mystical", null, ModuleMysticalAgriculture.BLACKLIST).
			setOreTypes(EnumOreType.values()).
			setProperties(MYSTICAL_PLANT_PROPERTIES);

	public static void register() {
		JAOPCAApi.registerModule(new ModuleMysticalAgriCraft());
	}

	@Override
	public String getName() {
		return "mysticalagricraft";
	}

	@Override
	public List<String> getDependencies() {
		return Lists.<String>newArrayList("mysticalagriculture");
	}

	@Override
	public List<? extends IItemRequest> getItemRequests() {
		return Lists.<ItemEntry>newArrayList(MYSTICAL_PLANT_ENTRY);
	}

	@Override
	public void registerConfigs(Configuration config) {
		for(IOreEntry entry : JAOPCAApi.ENTRY_NAME_TO_ORES_MAP.get("mystical")) {
			GROWTH_CHANCES.put(entry, config.get(Utils.to_under_score(entry.getOreName()), "maacGrowthChance", ModuleMysticalAgriculture.CROP_TIERS.get(entry) >= 6 ? 0.85D : 0.9D).setRequiresMcRestart(true).getDouble());
			GROWTH_BONUSES.put(entry, config.get(Utils.to_under_score(entry.getOreName()), "maacGrowthChance", ModuleMysticalAgriculture.CROP_TIERS.get(entry) >= 6 ? 0.02D : 0.025D).setRequiresMcRestart(true).getDouble());
		}
	}
}
