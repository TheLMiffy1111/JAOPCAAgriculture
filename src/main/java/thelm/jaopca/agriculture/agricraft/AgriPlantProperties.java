package thelm.jaopca.agriculture.agricraft;

import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import thelm.jaopca.api.EnumEntryType;
import thelm.jaopca.api.EnumOreType;
import thelm.jaopca.api.IOreEntry;
import thelm.jaopca.api.IProperties;

public class AgriPlantProperties implements IProperties {

	public static final AgriPlantProperties DEFAULT = new AgriPlantProperties();

	public List<Function<EnumOreType, String>> seedFormatFuncs = Lists.<Function<EnumOreType, String>>newArrayList();
	public ToDoubleFunction<IOreEntry> growthChanceFunc = entry->0.9D;
	public ToDoubleFunction<IOreEntry> growthBonusFunc = entry->0.025D;
	public boolean fertilizable = false;
	public boolean weed = false;
	public boolean aggressive = false;
	public ToDoubleFunction<IOreEntry> spreadChanceFunc = entry->0.1D;
	public ToDoubleFunction<IOreEntry> spawnChanceFunc = entry->0D;
	public ToDoubleFunction<IOreEntry> grassDropChanceFunc = entry->0D;
	public ToDoubleFunction<IOreEntry> seedDropChanceFunc = entry->1D;
	public ToDoubleFunction<IOreEntry> seedDropBonusFunc = entry->0D;
	public List<Product> products = Lists.<Product>newArrayList();
	public int minLight = 10;
	public int maxLight = 16;
	public List<String> soils = Lists.<String>newArrayList();
	public List<Condition> conditions = Lists.<Condition>newArrayList();
	public EnumRenderType renderType = EnumRenderType.HASH;
	public List<List<Pair<String, Boolean>>> textures = Lists.<List<Pair<String, Boolean>>>newArrayList();
	public Class<? extends IDummyAgriPlantWithProperty> dummyAgriPlantClass = DummyAgriPlantBase.class;
	public Class<? extends IAgriPlantWithProperty> agriPlantClass = AgriPlantBase.class;

	public AgriPlantProperties setSeedFormatFuncs(List<Function<EnumOreType, String>> value) {
		this.seedFormatFuncs = value;
		return this;
	}

	public AgriPlantProperties setSeedFormatFuncs(Function<EnumOreType, String>... value) {
		this.seedFormatFuncs = Lists.<Function<EnumOreType, String>>newArrayList(value);
		return this;
	}

	public AgriPlantProperties setGrowthChanceFunc(ToDoubleFunction<IOreEntry> value) {
		this.growthChanceFunc = value;
		return this;
	}

	public AgriPlantProperties setGrowthBonusFunc(ToDoubleFunction<IOreEntry> value) {
		this.growthBonusFunc = value;
		return this;
	}

	public AgriPlantProperties setFertilizable(boolean value) {
		this.fertilizable = value;
		return this;
	}

	public AgriPlantProperties setWeed(boolean value) {
		this.weed = value;
		return this;
	}

	public AgriPlantProperties setAggressive(boolean value) {
		this.aggressive = value;
		return this;
	}

	public AgriPlantProperties setSpreadChanceFunc(ToDoubleFunction<IOreEntry> value) {
		this.spreadChanceFunc = value;
		return this;
	}

	public AgriPlantProperties setSpawnChanceFunc(ToDoubleFunction<IOreEntry> value) {
		this.spawnChanceFunc = value;
		return this;
	}

	public AgriPlantProperties setGrassDropChanceFunc(ToDoubleFunction<IOreEntry> value) {
		this.grassDropChanceFunc = value;
		return this;
	}

	public AgriPlantProperties setSeedDropChanceFunc(ToDoubleFunction<IOreEntry> value) {
		this.seedDropChanceFunc = value;
		return this;
	}

	public AgriPlantProperties setSeedDropBonusFunc(ToDoubleFunction<IOreEntry> value) {
		this.seedDropBonusFunc = value;
		return this;
	}

	public AgriPlantProperties setProducts(List<Product> value) {
		this.products = value;
		return this;
	}

	public AgriPlantProperties setProducts(Product... value) {
		this.products = Lists.<Product>newArrayList(value);
		return this;
	}

	public AgriPlantProperties setMinLight(int value) {
		this.minLight = value;
		return this;
	}

	public AgriPlantProperties setMaxLight(int value) {
		this.maxLight = value;
		return this;
	}

	public AgriPlantProperties setSoils(List<String> value) {
		this.soils = value;
		return this;
	}

	public AgriPlantProperties setSoils(String... value) {
		this.soils = Lists.<String>newArrayList(value);
		return this;
	}

	public AgriPlantProperties setConditions(List<Condition> value) {
		this.conditions = value;
		return this;
	}

	public AgriPlantProperties setConditions(Condition... value) {
		this.conditions = Lists.<Condition>newArrayList(value);
		return this;
	}

	public AgriPlantProperties setRenderType(EnumRenderType value) {
		this.renderType = value;
		return this;
	}

	public AgriPlantProperties setTextures(List<List<Pair<String, Boolean>>> value) {
		this.textures = value;
		return this;
	}

	public AgriPlantProperties setTextures(List<Pair<String, Boolean>>... value) {
		this.textures = Lists.<List<Pair<String, Boolean>>>newArrayList(value);
		return this;
	}

	public AgriPlantProperties setDummyAgriPlantClass(Class<? extends IDummyAgriPlantWithProperty> value) {
		this.dummyAgriPlantClass = value;
		return this;
	}

	public AgriPlantProperties setAgriPlantClass(Class<? extends IAgriPlantWithProperty> value) {
		this.agriPlantClass = value;
		return this;
	}

	@Override
	public EnumEntryType getType() {
		return ModuleAgriCraft.AGRI_PLANT;
	}

	public static class Product {
		public ToIntFunction<IOreEntry> minFunc = entry->5;
		public ToIntFunction<IOreEntry> rangeFunc = entry->0;
		public ToDoubleFunction<IOreEntry> chanceFunc = entry->0.99D;
		public Function<EnumOreType, String> formatFunc;

		public Product setMinFunc(ToIntFunction<IOreEntry> value) {
			this.minFunc = value;
			return this;
		}

		public Product setRangeFunc(ToIntFunction<IOreEntry> value) {
			this.rangeFunc = value;
			return this;
		}

		public Product setChanceFunc(ToDoubleFunction<IOreEntry> value) {
			this.chanceFunc = value;
			return this;
		}

		public Product setFormatFunc(Function<EnumOreType, String> value) {
			this.formatFunc = value;
			return this;
		}
	}

	public static class Condition {
		public ToIntFunction<IOreEntry> amountFunc = entry->1;
		public int minX = 0;
		public int minY = -2;
		public int minZ = 0;
		public int maxX = 0;
		public int maxY = -2;
		public int maxZ = 0;
		public Function<EnumOreType, String> formatFunc;

		public Condition setAmountFunc(ToIntFunction<IOreEntry> value) {
			this.amountFunc = value;
			return this;
		}

		public Condition setMinX(int value) {
			this.minX = value;
			return this;
		}

		public Condition setMinY(int value) {
			this.minY = value;
			return this;
		}

		public Condition setMinZ(int value) {
			this.minZ = value;
			return this;
		}

		public Condition setMaxX(int value) {
			this.maxX = value;
			return this;
		}

		public Condition setMaxY(int value) {
			this.maxY = value;
			return this;
		}

		public Condition setMaxZ(int value) {
			this.maxZ = value;
			return this;
		}

		public Condition setRange(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
			this.minX = minX;
			this.minY = minY;
			this.minZ = minZ;
			this.maxX = maxX;
			this.maxY = maxY;
			this.maxZ = maxZ;
			return this;
		}

		public Condition setFormatFunc(Function<EnumOreType, String> value) {
			this.formatFunc = value;
			return this;
		}
	}
}
