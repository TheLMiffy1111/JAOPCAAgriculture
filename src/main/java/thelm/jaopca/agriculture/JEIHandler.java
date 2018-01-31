package thelm.jaopca.agriculture;

import java.util.ArrayList;
import java.util.List;

import com.blakebr0.mysticalagradditions.jei.Tier6CropWrapper;
import com.google.common.collect.Lists;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import thelm.jaopca.api.IOreEntry;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.utils.Utils;

@JEIPlugin
public class JEIHandler extends BlankModPlugin {

	@Override
	public void register(IModRegistry registry) {
		registry.addRecipes(getExtraRecipes());
	}

	public static List<IRecipeWrapper> getExtraRecipes() {
		ArrayList<IRecipeWrapper> ret = Lists.<IRecipeWrapper>newArrayList();
		for(IOreEntry entry : JAOPCAApi.ENTRY_NAME_TO_ORES_MAP.get("crux")) {
			ItemStack input = Utils.getOreStack("seeds", entry, 1);
			ItemStack crop = Utils.getOreStack("crops", entry, 1);
			ItemStack root = Utils.getOreStack("crux", entry, 1);
			ItemStack output = Utils.getOreStack("essence", entry, 1);

			ret.add(new Tier6CropWrapper(input, crop, root, output));
		}
		return ret;
	}
}
