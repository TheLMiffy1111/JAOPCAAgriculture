package thelm.jaopca.agriculture;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thelm.jaopca.api.IOreEntry;
import thelm.jaopca.api.ItemEntry;
import thelm.jaopca.api.item.ItemBase;

public class ItemEssenceBase extends ItemBase {

	public ItemEssenceBase(ItemEntry itemEntry, IOreEntry oreEntry) {
		super(itemEntry, oreEntry);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelResourceLocation mrl = null;
		switch(this.getOreEntry().getOreType()) {
		case DUST:
			mrl =  new ModelResourceLocation("jaopca:essence_dust#inventory");
			break;
		case GEM:
		case GEM_ORELESS:
			mrl = new ModelResourceLocation("jaopca:essence_gem#inventory");
			break;
		default:
			mrl = new ModelResourceLocation("jaopca:essence_ingot#inventory");
			break;
		}
		ModelLoader.setCustomModelResourceLocation(this, 0, mrl);
	}
}
