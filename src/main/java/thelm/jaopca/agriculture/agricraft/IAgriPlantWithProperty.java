package thelm.jaopca.agriculture.agricraft;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thelm.jaopca.api.IObjectWithProperty;

public interface IAgriPlantWithProperty extends IObjectWithProperty {

	default IAgriPlantWithProperty setSeedFormats(List<String> seedFormats) {
		return this;
	}

	default IAgriPlantWithProperty setGrowthChance(double growthChance) {
		return this;
	}

	default IAgriPlantWithProperty setGrowthBonus(double growthBonus) {
		return this;
	}

	default IAgriPlantWithProperty setFertilizable(boolean fertilizable) {
		return this;
	}

	default IAgriPlantWithProperty setWeed(boolean weed) {
		return this;
	}

	default IAgriPlantWithProperty setAggressive(boolean aggressive) {
		return this;
	}

	default IAgriPlantWithProperty setSpreadChance(double spreadChance) {
		return this;
	}

	default IAgriPlantWithProperty setSpawnChance(double spawnChance) {
		return this;
	}

	default IAgriPlantWithProperty setGrassDropChance(double grassDropChance) {
		return this;
	}

	default IAgriPlantWithProperty setSeedDropChance(double seedDropChance) {
		return this;
	}

	default IAgriPlantWithProperty setSeedDropBonus(double seedDropBonus) {
		return this;
	}

	default IAgriPlantWithProperty setProducts(List<Product> products) {
		return this;
	}

	default IAgriPlantWithProperty setMinLight(int minLight) {
		return this;
	}

	default IAgriPlantWithProperty setMaxLight(int maxLight) {
		return this;
	}

	default IAgriPlantWithProperty setSoils(List<String> soils) {
		return this;
	}

	default IAgriPlantWithProperty setConditions(List<Condition> conditions) {
		return this;
	}

	default IAgriPlantWithProperty setRenderType(EnumRenderType renderType) {
		return this;
	}

	default IAgriPlantWithProperty setTextures(List<List<Pair<String, Boolean>>> textures) {
		return this;
	}

	@SideOnly(Side.CLIENT)
	@Override
	default void registerModels() {}

	public static class Product {
		public int min = 5;
		public int range = 0;
		public double chance = 0.99D;
		public String format;

		public Product setMin(int min) {
			this.min = min;
			return this;
		}

		public Product setRange(int range) {
			this.range = range;
			return this;
		}

		public Product setChance(double chance) {
			this.chance = chance;
			return this;
		}

		public Product setFormat(String format) {
			this.format = format;
			return this;
		}
	}

	public static class Condition {
		public int amount = 1;
		public int minX = 0;
		public int minY = -2;
		public int minZ = 0;
		public int maxX = 0;
		public int maxY = -2;
		public int maxZ = 0;
		public String format;

		public Condition setAmount(int amount) {
			this.amount = amount;
			return this;
		}

		public Condition setMinX(int minX) {
			this.minX = minX;
			return this;
		}

		public Condition setMinY(int minY) {
			this.minY = minY;
			return this;
		}

		public Condition setMinZ(int minZ) {
			this.minZ = minZ;
			return this;
		}

		public Condition setMaxX(int maxX) {
			this.maxX = maxX;
			return this;
		}

		public Condition setMaxY(int maxY) {
			this.maxY = maxY;
			return this;
		}

		public Condition setMaxZ(int maxZ) {
			this.maxZ = maxZ;
			return this;
		}

		public Condition setFormat(String format) {
			this.format = format;
			return this;
		}
	}
}
