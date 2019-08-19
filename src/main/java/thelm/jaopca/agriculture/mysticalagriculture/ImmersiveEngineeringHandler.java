package thelm.jaopca.agriculture.mysticalagriculture;

import blusunrize.immersiveengineering.api.tool.BelljarHandler;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class ImmersiveEngineeringHandler {

	public static void registerBelljarCrop(ItemStack seeds, ItemStack essence, Block crop) {
		BelljarHandler.cropHandler.register(seeds, new ItemStack[] {essence}, new ItemStack(Blocks.DIRT), crop.getDefaultState());
	}
}
