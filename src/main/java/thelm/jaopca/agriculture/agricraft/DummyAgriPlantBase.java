package thelm.jaopca.agriculture.agricraft;

import com.agricraft.agricore.plant.AgriPlant;

import thelm.jaopca.api.IOreEntry;
import thelm.jaopca.api.ItemEntry;
import thelm.jaopca.api.utils.Utils;

public class DummyAgriPlantBase extends AgriPlant implements IDummyAgriPlantWithProperty {

	public final IOreEntry oreEntry;
	public final ItemEntry itemEntry;

	public DummyAgriPlantBase(ItemEntry itemEntry, IOreEntry oreEntry) {
		super();
		this.itemEntry = itemEntry;
		this.oreEntry = oreEntry;
	}

	@Override
	public ItemEntry getItemEntry() {
		return this.itemEntry;
	}

	@Override
	public IOreEntry getOreEntry() {
		return this.oreEntry;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public String getId() {
		return Utils.to_under_score(this.getOreEntry().getOreName())+'_'+this.getItemEntry().name+"_plant";
	}

	@Override
	public boolean validate() {
		return true;
	}
}
