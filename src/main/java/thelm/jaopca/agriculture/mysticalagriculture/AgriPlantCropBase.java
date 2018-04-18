package thelm.jaopca.agriculture.mysticalagriculture;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.infinityraider.agricraft.api.v1.AgriApi;
import com.infinityraider.agricraft.api.v1.requirement.BlockCondition;
import com.infinityraider.agricraft.api.v1.requirement.IGrowthReqBuilder;
import com.infinityraider.agricraft.api.v1.requirement.IGrowthRequirement;
import com.infinityraider.agricraft.api.v1.util.BlockRange;
import com.infinityraider.agricraft.api.v1.util.FuzzyStack;
import com.infinityraider.agricraft.farming.growthrequirement.GrowthReqBuilder;

import thelm.jaopca.agriculture.agricraft.AgriPlantBase;
import thelm.jaopca.api.IOreEntry;
import thelm.jaopca.api.ItemEntry;
import thelm.jaopca.api.utils.Utils;

public class AgriPlantCropBase extends AgriPlantBase {

	public static final List<List<Pair<String, Boolean>>> INGOT_TEXTURES = Lists.newArrayList(
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_0", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_1", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_2", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_2", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_3", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_4", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_4", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_ingot_overlay", false), Pair.of("jaopca:blocks/crops_ingot", true))
			);
	public static final List<List<Pair<String, Boolean>>> GEM_TEXTURES = Lists.newArrayList(
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_0", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_1", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_2", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_2", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_3", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_4", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_4", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_gem_overlay", false), Pair.of("jaopca:blocks/crops_gem", true))
			);
	public static final List<List<Pair<String, Boolean>>> DUST_TEXTURES = Lists.newArrayList(
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_0", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_1", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_2", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_2", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_3", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_4", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_4", false)),
			Lists.newArrayList(Pair.of("jaopca:blocks/crops_dust_overlay", false), Pair.of("jaopca:blocks/crops_dust", true))
			);

	public AgriPlantCropBase(ItemEntry itemEntry, IOreEntry oreEntry) {
		super(itemEntry, oreEntry);
	}

	@Override
	public double getGrowthChanceBase() {
		return ModuleMysticalAgriCraft.GROWTH_CHANCES.get(this.getOreEntry());
	}

	@Override
	public double getGrowthChanceBonus() {
		return ModuleMysticalAgriCraft.GROWTH_BONUSES.get(this.getOreEntry());
	}

	@Override
	public IGrowthRequirement getGrowthRequirement() {
		if(this.growthRequirement == null) {
			IGrowthReqBuilder builder = new GrowthReqBuilder();
			builder.addSoil(AgriApi.getSoilRegistry().get("farmland_soil").get());
			if(ModuleMysticalAgriculture.CROP_TIERS.get(this.getOreEntry()) >= 6 && ModuleMysticalAgriculture.ADDITIONS_LOADED) {
				builder.addCondition(new BlockCondition(new FuzzyStack(Utils.getOreStack("crux", this.getOreEntry(), 1)), new BlockRange(0, -2, 0, 0, -2, 0)));
			}
			builder.setMinLight(this.minLight);
			builder.setMaxLight(this.maxLight);
			this.growthRequirement = builder.build();
		}
		return this.growthRequirement;
	}

	@Override
	public AgriPlantBase setTextures(List<List<Pair<String, Boolean>>> textures) {
		switch(this.getOreEntry().getOreType()) {
		default:
		case INGOT:
		case INGOT_ORELESS:
			this.textures = INGOT_TEXTURES;
			break;
		case GEM:
		case GEM_ORELESS:
			this.textures = GEM_TEXTURES;
			break;
		case DUST:
			this.textures = DUST_TEXTURES;
			break;
		}
		return this;
	}
}
