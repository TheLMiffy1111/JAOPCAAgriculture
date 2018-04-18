package thelm.jaopca.agriculture.agricraft;

import java.util.stream.Collectors;

import com.agricraft.agricore.core.AgriCore;
import com.agricraft.agricore.plant.AgriPlant;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.infinityraider.agricraft.api.v1.plant.IAgriPlant;

import net.minecraft.util.ResourceLocation;
import thelm.jaopca.agriculture.agricraft.IAgriPlantWithProperty.Condition;
import thelm.jaopca.agriculture.agricraft.IAgriPlantWithProperty.Product;
import thelm.jaopca.api.IOreEntry;
import thelm.jaopca.api.ItemEntry;
import thelm.jaopca.api.JAOPCAApi;

public class RegistryAgriPlants {

	public static final HashBasedTable<String, String, IAgriPlant> AGRI_PLANTS_TABLE = HashBasedTable.<String, String, IAgriPlant>create();

	public static boolean checkAgriPlantEntry(ItemEntry entry, IOreEntry ore) {
		return false;
	}

	public static void registerAgriPlants(ItemEntry entry) {
		AgriPlantProperties ppt = (AgriPlantProperties)entry.properties;

		JAOPCAApi.TEXTURES.addAll(ppt.textures.stream().flatMap(list->list.stream()).map(pair->new ResourceLocation(pair.getLeft())).collect(Collectors.toList()));

		for(IOreEntry ore : JAOPCAApi.ENTRY_NAME_TO_ORES_MAP.get(entry.name)) {
			try {
				IDummyAgriPlantWithProperty dummyAgriPlant = ppt.dummyAgriPlantClass.getConstructor(ItemEntry.class, IOreEntry.class).newInstance(entry, ore);
				AgriCore.getPlants().addPlant((AgriPlant)dummyAgriPlant);
				IAgriPlantWithProperty agriPlant = ppt.agriPlantClass.getConstructor(ItemEntry.class, IOreEntry.class).newInstance(entry, ore);
				agriPlant.
				setSeedFormats(Lists.transform(ppt.seedFormatFuncs, func->func.apply(ore.getOreType()))).
				setGrowthChance(ppt.growthChanceFunc.applyAsDouble(ore)).
				setGrowthBonus(ppt.growthBonusFunc.applyAsDouble(ore)).
				setFertilizable(ppt.fertilizable).
				setWeed(ppt.weed).
				setAggressive(ppt.aggressive).
				setSpreadChance(ppt.spreadChanceFunc.applyAsDouble(ore)).
				setSpawnChance(ppt.spawnChanceFunc.applyAsDouble(ore)).
				setGrassDropChance(ppt.grassDropChanceFunc.applyAsDouble(ore)).
				setSeedDropChance(ppt.seedDropChanceFunc.applyAsDouble(ore)).
				setSeedDropBonus(ppt.seedDropBonusFunc.applyAsDouble(ore)).
				setProducts(Lists.transform(ppt.products, product->new Product().
						setMin(product.minFunc.applyAsInt(ore)).
						setRange(product.rangeFunc.applyAsInt(ore)).
						setChance(product.chanceFunc.applyAsDouble(ore)).
						setFormat(product.formatFunc.apply(ore.getOreType())))).
				setMinLight(ppt.minLight).
				setMaxLight(ppt.maxLight).
				setSoils(ppt.soils).
				setConditions(Lists.transform(ppt.conditions, condition->new Condition().
						setAmount(condition.amountFunc.applyAsInt(ore)).
						setMinX(condition.minX).
						setMinY(condition.minY).
						setMinZ(condition.minZ).
						setMaxX(condition.maxX).
						setMaxY(condition.maxY).
						setMaxZ(condition.maxZ).
						setFormat(condition.formatFunc.apply(ore.getOreType())))).
				setRenderType(ppt.renderType).
				setTextures(ppt.textures);
				AGRI_PLANTS_TABLE.put(entry.name, ore.getOreName(), (IAgriPlant)agriPlant);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
