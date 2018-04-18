package thelm.jaopca.agriculture.mysticalagriculture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.blakebr0.mysticalagriculture.config.ModConfig;
import com.blakebr0.mysticalagriculture.items.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thelm.jaopca.api.IOreEntry;
import thelm.jaopca.api.ItemEntry;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.block.IBlockWithProperty;
import thelm.jaopca.api.utils.JAOPCAStateMap;

public class BlockCropsBase extends BlockCrops implements IBlockWithProperty {

	public static final AxisAlignedBB CROPS_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);

	public final IOreEntry oreEntry;
	public final ItemEntry itemEntry;

	public int tier = -1;

	public BlockCropsBase(Material material, MapColor mapColor, ItemEntry itemEntry, IOreEntry oreEntry) {
		super();
		setDefaultState(this.blockState.getBaseState().withProperty(AGE, 0));
		setUnlocalizedName("jaopca."+itemEntry.name);
		setRegistryName("jaopca:block_"+itemEntry.name+oreEntry.getOreName());
		this.oreEntry = oreEntry;
		this.itemEntry = itemEntry;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		this.checkAndDropBlock(world, pos, state);
		if(this.getTier() >= 6 && world.getBlockState(pos.down(2)).getBlock() != getCrux()) {
			return;
		}
		int i = this.getAge(state);
		if(world.getLightFromNeighbors(pos.up()) >= 9 && i < this.getMaxAge()) {
			float f = getGrowthChance(this, world, pos);
			if(rand.nextInt((int) (35.0F / f) + 1) == 0) {
				world.setBlockState(pos, this.withAge(i + 1), 2);
			}
		}
	}

	@Override
	public IOreEntry getOreEntry() {
		return oreEntry;
	}

	@Override
	public ItemEntry getItemEntry() {
		return itemEntry;
	}

	public int getTier() {
		if(tier == -1) {
			tier = ModuleMysticalAgriculture.CROP_TIERS.get(this.getOreEntry());
		}
		return tier;
	}

	@Override
	protected boolean canSustainBush(IBlockState state) {
		return state.getBlock() == Blocks.FARMLAND;
	}

	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
		return false;
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Crop;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return CROPS_AABB;
	}

	protected Block crux = null;

	public Block getCrux() {
		if(getTier() >= 6 && crux == null) {
			crux = JAOPCAApi.BLOCKS_TABLE.get("crux", this.getOreEntry().getOreName());
		}
		if(crux != null) {
			return crux;
		}
		return Blocks.AIR;
	}

	protected Item seed = null;

	@Override
	public Item getSeed() {
		if(seed == null) {
			seed = JAOPCAApi.ITEMS_TABLE.get("seeds", this.getOreEntry().getOreName());
		}
		return seed;
	}

	protected Item essence = null;

	@Override
	public Item getCrop() {
		if(essence == null) {
			essence = JAOPCAApi.ITEMS_TABLE.get("essence", this.getOreEntry().getOreName());
		}
		return essence;
	}

	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ArrayList drops = new ArrayList();
		int age = state.getValue(AGE);
		Random rand = RANDOM;
		int essence = 1;
		int fertilizer = 0;
		int seeds = 1;

		if(age == 7) {
			if(this.getTier() < 6 && ModConfig.confSeedChance > 0) {
				if(rand.nextInt(100 / ModConfig.confSeedChance) > 0) {
					seeds = 1;
				}
				else {
					seeds = 2;
				}
			}

			if(ModConfig.confFertilizedEssenceChance > 0) {
				if(rand.nextInt(100 / ModConfig.confFertilizedEssenceChance) > 0) {
					fertilizer = 0;
				}
				else {
					fertilizer = 1;
				}
			}

			if(ModConfig.confEssenceChance > 0) {
				if(rand.nextInt(100 / ModConfig.confEssenceChance) > 0) {
					essence = 1;
				}
				else {
					essence = 2;
				}
			}
		}

		drops.add(new ItemStack(this.getSeed(), seeds, 0));
		if(essence > 0) {
			drops.add(new ItemStack(this.getCrop(), essence, 0));
		}
		if(fertilizer > 0 && ModConfig.confFertilizedEssence) {
			drops.add(new ItemStack(ModItems.itemFertilizedEssence, fertilizer, 0));
		}

		return drops;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ResourceLocation rl = null;
		switch(this.getOreEntry().getOreType()) {
		case DUST:
			rl = new ResourceLocation("jaopca:crops_dust");
			break;
		case GEM:
		case GEM_ORELESS:
			rl = new ResourceLocation("jaopca:crops_gem");
			break;
		default:
			rl = new ResourceLocation("jaopca:crops_ingot");
			break;
		}
		ModelLoader.setCustomStateMapper(this, new JAOPCAStateMap.Builder(rl).build());
	}

	/*=========================================================================================*/

	@Override
	public BlockCropsBase setSoundType(SoundType sound) {
		super.setSoundType(sound);
		return this;
	}

	@Override
	public BlockCropsBase setLightOpacity(int opacity) {
		super.setLightOpacity(opacity);
		return this;
	}

	@Override
	public BlockCropsBase setLightLevel(float value) {
		super.setLightLevel(value);
		return this;
	}

	@Override
	public BlockCropsBase setResistance(float resistance) {
		super.setResistance(resistance);
		return this;
	}

	@Override
	public BlockCropsBase setHardness(float hardness) {
		super.setHardness(hardness);
		return this;
	}
}
