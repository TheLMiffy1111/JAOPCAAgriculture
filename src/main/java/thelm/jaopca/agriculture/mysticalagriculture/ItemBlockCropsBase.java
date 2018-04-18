package thelm.jaopca.agriculture.mysticalagriculture;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thelm.jaopca.api.block.IBlockWithProperty;
import thelm.jaopca.api.item.ItemBlockBase;

public class ItemBlockCropsBase extends ItemBlockBase {

	public ItemBlockCropsBase(IBlockWithProperty block) {
		super(block);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelResourceLocation mrl = null;
		switch(this.getOreEntry().getOreType()) {
		case DUST:
			mrl = new ModelResourceLocation("jaopca:crops_dust#inventory");
			break;
		case GEM:
		case GEM_ORELESS:
			mrl = new ModelResourceLocation("jaopca:crops_gem#inventory");
			break;
		default:
			mrl = new ModelResourceLocation("jaopca:crops_ingot#inventory");
			break;
		}
		ModelLoader.setCustomModelResourceLocation(this, 0, mrl);
	}
}
