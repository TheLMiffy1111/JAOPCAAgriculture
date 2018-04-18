package thelm.jaopca.agriculture.mysticalagriculture;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thelm.jaopca.api.IOreEntry;
import thelm.jaopca.api.ItemEntry;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.item.ItemBase;

public class ItemSeedsBase extends ItemBase implements IPlantable {

	public int tier = -1;

	public ItemSeedsBase(ItemEntry itemEntry, IOreEntry oreEntry) {
		super(itemEntry, oreEntry);
	}

	public int getTier() {
		if(tier == -1) {
			tier = ModuleMysticalAgriculture.CROP_TIERS.get(this.getOreEntry());
		}
		return tier;
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState state = worldIn.getBlockState(pos);
		if(facing == EnumFacing.UP && playerIn.canPlayerEdit(pos.offset(facing), facing, stack) && state.getBlock().canSustainPlant(state, worldIn, pos, EnumFacing.UP, this) && worldIn.isAirBlock(pos.up())) {
			worldIn.setBlockState(pos.up(), this.getPlant(worldIn, pos));
			--stack.stackSize;
			return EnumActionResult.SUCCESS;
		}
		else {
			return EnumActionResult.FAIL;
		}
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Crop;
	}

	protected Block crops = null;

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		if(crops == null) {
			crops = JAOPCAApi.BLOCKS_TABLE.get("crops", this.getOreEntry().getOreName());
		}
		if(crops != null) {
			return crops.getDefaultState();
		}
		return Blocks.AIR.getDefaultState();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean advanced) {
		String text = I18n.translateToLocal("tooltip.ma.tier")+TextFormatting.GRAY+": ";
		int tier = this.getTier();
		switch(tier) {
		case 1:
			tooltip.add(text+TextFormatting.YELLOW+tier);
			break;
		case 2:
			tooltip.add(text+TextFormatting.GREEN+tier);
			break;
		case 3:
			tooltip.add(text+TextFormatting.GOLD+tier);
			break;
		case 4:
			tooltip.add(text+TextFormatting.AQUA+tier);
			break;
		case 5:
			tooltip.add(text+TextFormatting.RED+tier);
			break;
		case 6:
			tooltip.add(text+TextFormatting.DARK_PURPLE+tier);
		}
	}
}
