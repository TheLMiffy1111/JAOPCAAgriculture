package thelm.jaopca.agriculture.mysticalagriculture;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import com.google.common.collect.Lists;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import thelm.jaopca.api.IOreEntry;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.utils.Utils;

@JEIPlugin
public class JEIHandler extends BlankModPlugin {

	private static final Constructor<? extends IRecipeWrapper> WRAPPER_CONSTRUCTOR;

	@Override
	public void register(IModRegistry registry) {
		registry.addRecipes(getExtraRecipes());
	}

	public static List<IRecipeWrapper> getExtraRecipes() {
		ArrayList<IRecipeWrapper> ret = Lists.<IRecipeWrapper>newArrayList();
		if(ModuleMysticalAgriculture.ADDITIONS_LOADED) {
			for(IOreEntry entry : JAOPCAApi.ENTRY_NAME_TO_ORES_MAP.get("crux")) {
				ItemStack input = Utils.getOreStack("seeds", entry, 1);
				ItemStack crop = Utils.getOreStack("crops", entry, 1);
				ItemStack root = Utils.getOreStack("crux", entry, 1);
				ItemStack output = Utils.getOreStack("essence", entry, 1);

				try {
					ret.add(WRAPPER_CONSTRUCTOR.newInstance(input, crop, root, output));
				}
				catch(InstantiationException | IllegalAccessException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return ret;
	}

	static {
		if(Loader.isModLoaded("mysticalagradditions")) {
			WRAPPER_CONSTRUCTOR = new Callable<Constructor<? extends IRecipeWrapper>>() {
				@Override
				public Constructor<? extends IRecipeWrapper> call() {
					try {
						return (Constructor<? extends IRecipeWrapper>)Class.forName("com.blakebr0.mysticalagradditions.jei.Tier6CropWrapper").getConstructor(ItemStack.class, ItemStack.class, ItemStack.class, ItemStack.class);
					}
					catch(ClassNotFoundException | NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
					}
					return null;
				}
			}.call();
		}
		else {
			WRAPPER_CONSTRUCTOR = null;
		}
	}
}
