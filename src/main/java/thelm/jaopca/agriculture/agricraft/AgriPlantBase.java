package thelm.jaopca.agriculture.agricraft;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.infinityraider.agricraft.api.v1.AgriApi;
import com.infinityraider.agricraft.api.v1.crop.IAgriCrop;
import com.infinityraider.agricraft.api.v1.plant.IAgriPlant;
import com.infinityraider.agricraft.api.v1.render.RenderMethod;
import com.infinityraider.agricraft.api.v1.requirement.BlockCondition;
import com.infinityraider.agricraft.api.v1.requirement.IGrowthReqBuilder;
import com.infinityraider.agricraft.api.v1.requirement.IGrowthRequirement;
import com.infinityraider.agricraft.api.v1.stat.IAgriStat;
import com.infinityraider.agricraft.api.v1.util.BlockRange;
import com.infinityraider.agricraft.api.v1.util.FuzzyStack;
import com.infinityraider.agricraft.farming.PlantStats;
import com.infinityraider.agricraft.farming.growthrequirement.GrowthReqBuilder;
import com.infinityraider.agricraft.init.AgriItems;
import com.infinityraider.agricraft.reference.AgriNBT;
import com.infinityraider.infinitylib.render.tessellation.ITessellator;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thelm.jaopca.api.IOreEntry;
import thelm.jaopca.api.ItemEntry;
import thelm.jaopca.api.utils.Utils;

public class AgriPlantBase implements IAgriPlant, IAgriPlantWithProperty {

	public final IOreEntry oreEntry;
	public final ItemEntry itemEntry;

	public AgriPlantBase(ItemEntry itemEntry, IOreEntry oreEntry) {
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
	public String getId() {
		return Utils.to_under_score(this.getOreEntry().getOreName())+'_'+this.getItemEntry().name+"_plant";
	}

	public String getUnlocalizedPlantName() {
		return "plant.jaopca."+itemEntry.name;
	}

	@Override
	public String getPlantName() {
		return Utils.smartLocalize(this.getUnlocalizedPlantName(), this.getUnlocalizedPlantName()+".%s", this.getOreEntry());
	}

	public String getUnlocalizedInformation() {
		return "plant.desc.jaopca."+itemEntry.name;
	}

	@Override
	public String getInformation() {
		return Utils.smartLocalize(this.getUnlocalizedInformation(), this.getUnlocalizedInformation()+".%s", this.getOreEntry());
	}

	/*========================================= SEED =======================================*/

	protected List<String> seedFormats = Lists.<String>newArrayList();
	protected double grassDropChance = 0D;
	protected double seedDropChance = 1D;
	protected double seedDropBonus = 0D;

	public AgriPlantBase setSeedFormats(List<String> seedFormats) {
		this.seedFormats = seedFormats;
		return this;
	}

	@Override
	public AgriPlantBase setGrassDropChance(double grassDropChance) {
		this.grassDropChance = grassDropChance;
		return this;
	}

	@Override
	public AgriPlantBase setSeedDropChance(double seedDropChance) {
		this.seedDropChance = seedDropChance;
		return this;
	}

	@Override
	public AgriPlantBase setSeedDropBonus(double seedDropBonus) {
		this.seedDropBonus = seedDropBonus;
		return this;
	}

	protected Collection<FuzzyStack> seedItems = null;

	@Override
	public Collection<FuzzyStack> getSeedItems() {
		if(this.seedItems == null) {
			this.seedItems = this.seedFormats.stream().
					map(format->toFuzzyStack(format, this.getOreEntry(), 1)).
					collect(Collectors.toList());
		}
		return this.seedItems;
	}

	@Override
	public double getGrassDropChance() {
		return this.grassDropChance;
	}

	@Override
	public double getSeedDropChanceBase() {
		return this.seedDropChance;
	}

	@Override
	public double getSeedDropChanceBonus() {
		return this.seedDropBonus;
	}

	@Override
	public ItemStack getSeed() {
		ItemStack stack = this.getSeedItems().stream()
				.map(FuzzyStack::toStack)
				.findFirst()
				.orElse(new ItemStack(AgriItems.getInstance().AGRI_SEED));
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString(AgriNBT.SEED, this.getId());
		new PlantStats().writeToNBT(tag);
		stack.setTagCompound(tag);
		return stack;
	}

	@Override
	public String getSeedName() {
		return this.getSeed().getDisplayName();
	}

	/*======================================== GROWTH ======================================*/

	protected double growthChance = 0.9D;
	protected double growthBonus = 0.025D;
	protected boolean fertilizable = false;
	protected boolean weed = false;
	protected boolean aggressive = false;
	protected double spreadChance = 0.1D;
	protected double spawnChance = 0D;

	public AgriPlantBase setGrowthChance(double growthChance) {
		this.growthChance = growthChance;
		return this;
	}

	public AgriPlantBase setGrowthBonus(double growthBonus) {
		this.growthBonus = growthBonus;
		return this;
	}

	@Override
	public AgriPlantBase setFertilizable(boolean fertilizable) {
		this.fertilizable = fertilizable;
		return this;
	}

	@Override
	public AgriPlantBase setWeed(boolean weed) {
		this.weed = weed;
		return this;
	}

	@Override
	public AgriPlantBase setAggressive(boolean aggressive) {
		this.aggressive = aggressive;
		return this;
	}

	@Override
	public AgriPlantBase setSpreadChance(double spreadChance) {
		this.spreadChance = spreadChance;
		return this;
	}

	@Override
	public AgriPlantBase setSpawnChance(double spawnChance) {
		this.spawnChance = spawnChance;
		return this;
	}

	@Override
	public double getGrowthChanceBase() {
		return this.growthChance;
	}

	@Override
	public double getGrowthChanceBonus() {
		return this.growthBonus;
	}

	@Override
	public boolean isFertilizable() {
		return this.fertilizable;
	}

	@Override
	public boolean isWeed() {
		return this.weed;
	}

	@Override
	public boolean isAggressive() {
		return this.aggressive;
	}

	@Override
	public double getSpreadChance() {
		return this.spreadChance;
	}

	@Override
	public double getSpawnChance() {
		return this.spawnChance;
	}

	/*======================================== PRODUCT =====================================*/

	protected List<Product> products = Lists.<Product>newArrayList();

	@Override
	public AgriPlantBase setProducts(List<Product> products) {
		this.products = products;
		return this;
	}

	@Override
	public void getPossibleProducts(Consumer<ItemStack> consumer) {
		this.products.stream().
		map(product->toItemStack(product.format, this.getOreEntry(), 1)).
		forEach(consumer);
	}

	@Override
	public void getHarvestProducts(Consumer<ItemStack> consumer, IAgriCrop crop, IAgriStat stat, Random rand) {
		this.products.stream().
		filter(product->product.chance>rand.nextDouble()).
		map(product->toItemStack(product.format, this.getOreEntry(), product.min+rand.nextInt(product.range+1))).
		forEach(consumer);
	}

	/*====================================== REQUIREMENT ===================================*/

	protected int minLight = 10;
	protected int maxLight = 16;
	protected List<String> soils = Lists.<String>newArrayList();
	protected List<Condition> conditions = Lists.<Condition>newArrayList();

	@Override
	public AgriPlantBase setMinLight(int minLight) {
		this.minLight = minLight;
		return this;
	}

	@Override
	public AgriPlantBase setMaxLight(int maxLight) {
		this.maxLight = maxLight;
		return this;
	}

	@Override
	public AgriPlantBase setSoils(List<String> soils) {
		this.soils = soils;
		return this;
	}

	@Override
	public AgriPlantBase setConditions(List<Condition> conditions) {
		this.conditions = conditions;
		return this;
	}

	protected IGrowthRequirement growthRequirement = null;

	@Override
	public IGrowthRequirement getGrowthRequirement() {
		if(this.growthRequirement == null) {
			IGrowthReqBuilder builder = new GrowthReqBuilder();
			this.soils.stream().
			map(AgriApi.getSoilRegistry()::get).
			filter(Optional::isPresent).
			map(Optional::get).
			forEach(builder::addSoil);
			this.conditions.stream().
			map(condition->new BlockCondition(
					toFuzzyStack(condition.format, this.getOreEntry(), 1),
					new BlockRange(
							condition.minX, condition.minY, condition.minZ,
							condition.maxX, condition.maxY, condition.maxZ
							)
					)).
			forEach(builder::addCondition);
			builder.setMinLight(this.minLight);
			builder.setMaxLight(this.maxLight);
			this.growthRequirement = builder.build();
		}
		return this.growthRequirement;
	}

	/*======================================== TEXTURE =====================================*/

	protected List<List<Pair<String, Boolean>>> textures = Lists.<List<Pair<String, Boolean>>>newArrayList();

	@Override
	public AgriPlantBase setTextures(List<List<Pair<String, Boolean>>> textures) {
		this.textures = textures;
		return this;
	}

	@Override
	public int getGrowthStages() {
		return this.textures.size();
	}

	/*========================================= RENDER =======================================*/

	protected EnumRenderType renderType = EnumRenderType.HASH;

	@Override
	public AgriPlantBase setRenderType(EnumRenderType renderType) {
		this.renderType = renderType;
		return this;
	}

	@Override
	public float getHeight(int meta) {
		return 13/16F;
	}

    @SideOnly(Side.CLIENT)
	@Override
	public List<BakedQuad> getPlantQuads(IExtendedBlockState state, int growthStage, EnumFacing direction, Function<ResourceLocation, TextureAtlasSprite> textureToIcon) {
        if(textureToIcon instanceof ITessellator) {
        	JAOPCAPlantRenderer.renderPlant((ITessellator)textureToIcon, this.textures.get(growthStage), this.renderType, this.getOreEntry().getColor());
        }
        return Collections.<BakedQuad>emptyList();
	}

	/*========================================= OTHERS =======================================*/

    @SideOnly(Side.CLIENT)
	@Override
	public RenderMethod getRenderMethod() {
		return RenderMethod.CUSTOM;
	}

    @SideOnly(Side.CLIENT)
	@Override
	public ResourceLocation getSeedTexture() {
		return TextureMap.LOCATION_MISSING_TEXTURE;
	}

    @SideOnly(Side.CLIENT)
	@Override
	public ResourceLocation getPrimaryPlantTexture(int meta) {
		return new ResourceLocation(this.textures.get(meta).get(0).getLeft());
	}

    @SideOnly(Side.CLIENT)
	@Override
	public ResourceLocation getSecondaryPlantTexture(int meta) {
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof IAgriPlant) && (this.getId().equals(((IAgriPlant)obj).getId()));
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

	/*========================================== UTIL ========================================*/

	public static ItemStack toItemStack(String format, IOreEntry ore, int amount) {
		return Utils.getOreStack(String.format(format, ore.getOreName()), amount);
	}

	public static FuzzyStack toFuzzyStack(String format, IOreEntry ore, int amount) {
		return new FuzzyStack(toItemStack(format, ore, amount), false, false, "*");
	}
}
