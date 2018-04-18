package thelm.jaopca.agriculture.agricraft;

import com.infinityraider.agricraft.api.v1.IAgriPlugin;
import com.infinityraider.agricraft.api.v1.misc.IAgriRegistry;
import com.infinityraider.agricraft.api.v1.plant.IAgriPlant;
import com.infinityraider.agricraft.api.v1.plugin.AgriPlugin;

@AgriPlugin
public class JAOPCAPlugin implements IAgriPlugin {
	
	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public void registerPlants(IAgriRegistry<IAgriPlant> plantRegistry) {
		for(IAgriPlant plant : RegistryAgriPlants.AGRI_PLANTS_TABLE.values()) {
			plantRegistry.add(plant);
		}
	}
}
