package thelm.jaopca.agriculture.agricraft;

import thelm.jaopca.api.EnumEntryType;
import thelm.jaopca.api.ModuleBase;

//Currently only holds the AgriPlant entry type
public class ModuleAgriCraft extends ModuleBase {

	public static final EnumEntryType AGRI_PLANT = EnumEntryType.addEntryType("AGRI_PLANT", RegistryAgriPlants::checkAgriPlantEntry, RegistryAgriPlants::registerAgriPlants, AgriPlantProperties.DEFAULT, AgriPlantPropertiesDeserializer::parseAgriPlantPpt);

	public static void register() {}

	@Override
	public String getName() {
		return "agricraft";
	}
}
