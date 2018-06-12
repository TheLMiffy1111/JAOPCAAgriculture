package thelm.jaopca.agriculture.mysticalagriculture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.blakebr0.mysticalagriculture.crafting.ReprocessorManager;
import com.blakebr0.mysticalagriculture.items.ModItems;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import thelm.jaopca.api.EnumEntryType;
import thelm.jaopca.api.EnumOreType;
import thelm.jaopca.api.IItemRequest;
import thelm.jaopca.api.IOreEntry;
import thelm.jaopca.api.ItemEntry;
import thelm.jaopca.api.ItemEntryGroup;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.ModuleBase;
import thelm.jaopca.api.block.BlockProperties;
import thelm.jaopca.api.item.ItemProperties;
import thelm.jaopca.api.utils.Utils;

public class ModuleMysticalAgriculture extends ModuleBase {

	public static final boolean ADDITIONS_LOADED = Loader.isModLoaded("mysticalagradditions");

	public static final ArrayList<String> BLACKLIST;

	static {
		BLACKLIST = Lists.<String>newArrayList(
				"Coal", "Iron", "Quartz", "Glowstone", "Redstone", "Gold", "Lapis", "Diamond", "Emerald", "Silicon", "Sulfur", "Aluminium", "Copper", "Saltpeter",
				"Tin", "Bronze", "Zinc", "Brass", "Silver", "Lead", "Graphite", "Steel", "Nickel", "Constantan", "Electrum", "Invar", "Mithril", "Tungsten",
				"Titanium", "Uranium", "Chromium", "Platinum", "Iridium", "Ruby", "Sapphire", "Peridot", "Amber", "Topaz", "Malachite", "Tanzanite", "Signalum",
				"Enderium", "Lumium", "AluminiumBrass", "Knightslime", "Cobalt", "Ardite", "Manyullyn", "ElecticalSteel", "RedstoneAlloy", "ConductiveIron",
				"Soularium", "DarkSteel", "PulsatingIron", "EnergeticAlloy", "VibrantAlloy", "EndSteel", "Manasteel", "Terrasteel", "Thaumium", "Void",
				"Dawnstone", "Osmium", "RefinedGlowstone", "RefinedObsidian", "Aquarium", "Coldiron", "Starsteel", "Adamantine", "Apatite", "Steeleaf",
				"Ironwood", "Knightmetal", "Fiery", "MeteoricIron", "Desh", "Syrmorite", "Octine", "Valonite", "Thorium", "Boron", "Lithium", "Magnesium",
				"BlackQuartz", "Vinteum", "Chimerite", "BlueTopaz", "Moonstone", "Sunstone", "Aquamarine", "Starmetal", "RockCrystal", "EnderBiotite", "Dark",
				"CompressedIron", "Amethyst", "Draconium", "Yellorium", "CertusQuartz", "Fluix", "ChargedCertusQuartz", "QuartzEnrichedIron", "BaseEssence",
				"Inferium", "Prudentium", "Intermedium", "Superium", "Supremium", "Soulium", "Prismarine"
				);
		if(ADDITIONS_LOADED) {
			Collections.<String>addAll(BLACKLIST, "Insanium", "NetherStar", "DraconiumAwakened");
		}
	}

	public static final HashMap<IOreEntry, Integer> CROP_TIERS = Maps.<IOreEntry, Integer>newHashMap();
	public static final HashMap<IOreEntry, Pair<EnumRecipePattern, Integer>> RECIPE_PATTERNS = Maps.<IOreEntry, Pair<EnumRecipePattern, Integer>>newHashMap();
	public static final HashMap<IOreEntry, ItemStack> CRUX_SPECIALS = Maps.<IOreEntry, ItemStack>newHashMap();

	public static final ItemProperties ESSENCE_PROPERTIES = new ItemProperties().setItemClass(ItemEssenceBase.class);
	public static final ItemProperties SEEDS_PROPERTIES = new ItemProperties().setItemClass(ItemSeedsBase.class);
	public static final BlockProperties CROPS_PROPERTIES = new BlockProperties().
			setHardnessFunc(entry->0F).
			setLightOpacityFunc(entry->0).
			setSoundType(SoundType.PLANT).
			setBlockClass(BlockCropsBase.class).
			setItemBlockClass(ItemBlockCropsBase.class);
	public static final BlockProperties CRUX_PROPERTIES = new BlockProperties().
			setHardnessFunc(entry->5F).
			setResistanceFunc(entry->10F);

	public static final ItemEntry ESSENCE_ENTRY = new ItemEntry(EnumEntryType.ITEM, "essence", new ModelResourceLocation("jaopca:essence"), BLACKLIST).
			setOreTypes(EnumOreType.values()).
			setProperties(ESSENCE_PROPERTIES);
	public static final ItemEntry SEEDS_ENTRY = new ItemEntry(EnumEntryType.ITEM, "mysticalSeeds", new ModelResourceLocation("jaopca:seeds#inventory"), BLACKLIST).
			setOreTypes(EnumOreType.values()).
			setProperties(SEEDS_PROPERTIES);
	public static final ItemEntry CROPS_ENTRY = new ItemEntry(EnumEntryType.BLOCK, "mysticalCrops", new ModelResourceLocation("jaopca:crops"), BLACKLIST).
			setOreTypes(EnumOreType.values()).
			setProperties(CROPS_PROPERTIES);
	public static final ItemEntry CRUX_ENTRY = new ItemEntry(EnumEntryType.BLOCK, "crux", new ModelResourceLocation("jaopca:crux#normal"), BLACKLIST).
			setOreTypes(EnumOreType.values()).
			setProperties(CRUX_PROPERTIES);

	public ModuleMysticalAgriculture() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	public static void register() {
		JAOPCAApi.registerModule(new ModuleMysticalAgriculture());
		if(Loader.isModLoaded("agricraft")) {
			JAOPCAApi.registerModule(new ModuleMysticalAgriCraft());
		}
	}

	@Override
	public String getName() {
		return "mysticalagriculture";
	}

	@Override
	public void registerConfigsPre(Configuration config) {
		for(IOreEntry entry : JAOPCAApi.ORE_ENTRY_LIST) {
			if(!BLACKLIST.contains(entry.getOreName()) && !entry.getModuleBlacklist().contains(this.getName())) {
				int tier = config.get(Utils.to_under_score(entry.getOreName()), "maTier", Utils.rarityI(entry, 2)+1).setRequiresMcRestart(true).getInt();
				if(tier >= 6 && ADDITIONS_LOADED) {
					tier = MathHelper.clamp(tier, 1, 6);
				}
				else {
					tier = MathHelper.clamp(tier, 1, 5);
					CRUX_ENTRY.blacklist.add(entry.getOreName());
				}
				CROP_TIERS.put(entry, tier);
			}
		}
	}

	@Override
	public List<IItemRequest> getItemRequests() {
		return Lists.<IItemRequest>newArrayList(ItemEntryGroup.of(ESSENCE_ENTRY, SEEDS_ENTRY, CROPS_ENTRY), CRUX_ENTRY);
	}

	@Override
	public void registerConfigs(Configuration config) {
		for(IOreEntry entry : JAOPCAApi.ENTRY_NAME_TO_ORES_MAP.get("essence")) {
			String name = Utils.to_under_score(entry.getOreName());
			EnumRecipePattern fallback = EnumRecipePattern.BORDER;
			if(CROP_TIERS.get(entry) >= 6) {
				fallback = EnumRecipePattern.FULL;
			}
			else if(entry.getOreType() == EnumOreType.DUST) {
				fallback = EnumRecipePattern.HORIZONTAL;
			}

			EnumRecipePattern pattern = EnumRecipePattern.getPattern(config.get(name, "maInputAmount", fallback.amount).setRequiresMcRestart(true).getInt(), fallback);
			int outputDefault = 0;
			if(CROP_TIERS.get(entry) >= 6) {
				outputDefault = (int)(3*((double)fallback.amount/(double)pattern.amount));
			}
			else {
				switch(entry.getOreType()) {
				case INGOT:
				case INGOT_ORELESS:
					outputDefault = (int)(4*((double)fallback.amount/(double)pattern.amount));
					break;
				case DUST:
					outputDefault = (int)(5*((double)fallback.amount/(double)pattern.amount));
					break;
				case GEM:
				case GEM_ORELESS:
					outputDefault = (int)(6*((double)fallback.amount/(double)pattern.amount));
					break;
				}
			}
			Pair<EnumRecipePattern, Integer> toPut = Pair.of(pattern, config.get(name, "maOutputAmount", outputDefault).setRequiresMcRestart(true).getInt());
			RECIPE_PATTERNS.put(entry, toPut);
		}

		for(IOreEntry entry : JAOPCAApi.ENTRY_NAME_TO_ORES_MAP.get("crux")) {
			String s = "ingot";
			if(Utils.doesOreNameExist("block"+entry.getOreName())) {
				s = "block";
			}
			else {
				switch(entry.getOreType()) {
				case DUST:
					s = "dust";
					break;
				case GEM:
				case GEM_ORELESS:
					s = "gem";
					break;
				default:
					break;
				}
			}
			ItemStack stk = Utils.getOreStack(s, entry, 1);
			String special = stk.getItem().getRegistryName().toString()+"@"+stk.getItemDamage();
			special = config.get(Utils.to_under_score(entry.getOreName()), "maCruxSpecial", special).setRequiresMcRestart(true).getString();
			CRUX_SPECIALS.put(entry, Utils.parseItemStack(special));
		}
	}

	@Override
	public void preInit() {
		for(IOreEntry entry : JAOPCAApi.ENTRY_NAME_TO_ORES_MAP.get("essence")) {
			OreDictionary.registerOre("essenceTier"+CROP_TIERS.get(entry), Utils.getOreStack("essence", entry, 1));
		}

		for(IOreEntry entry : JAOPCAApi.ENTRY_NAME_TO_ORES_MAP.get("mysticalSeeds")) {
			OreDictionary.registerOre("seedsTier"+CROP_TIERS.get(entry), Utils.getOreStack("mysticalSeeds", entry, 1));
		}
	}

	@Override
	public void init() {
		for(IOreEntry entry : JAOPCAApi.ENTRY_NAME_TO_ORES_MAP.get("essence")) {
			ReprocessorManager.addRecipe(Utils.getOreStack("essence", entry, 2), Utils.getOreStack("mysticalSeeds", entry, 1));

			Pair<EnumRecipePattern, Integer> recipe = RECIPE_PATTERNS.get(entry);
			String s = "ingot";
			if(CROP_TIERS.get(entry) >= 6 && Utils.doesOreNameExist("nugget"+entry.getOreName())) {
				s = "nugget";
			}
			else {
				switch(entry.getOreType()) {
				case DUST:
					s = "dust";
					break;
				case GEM:
				case GEM_ORELESS:
					s = "gem";
					break;
				default:
					break;
				}
			}
			Utils.addShapedOreRecipe(Utils.getOreStack(s, entry, recipe.getRight()), recipe.getLeft().getRecipePattern("essence"+entry.getOreName()));
		}

		for(IOreEntry entry : JAOPCAApi.ENTRY_NAME_TO_ORES_MAP.get("mysticalSeeds")) {
			String s = "ingot";
			switch(entry.getOreType()) {
			case DUST:
				s = "dust";
				break;
			case GEM:
			case GEM_ORELESS:
				s = "gem";
				break;
			default:
				break;
			}
			Utils.addShapedOreRecipe(Utils.getOreStack("mysticalSeeds", entry, 1), new Object[] {
					"MEM",
					"ESE",
					"MEM",
					'M', s+entry.getOreName(),
					'E', getEssence(CROP_TIERS.get(entry)),
					'S', getCraftingSeeds(CROP_TIERS.get(entry)),
			});
		}

		for(IOreEntry entry : JAOPCAApi.ENTRY_NAME_TO_ORES_MAP.get("crux")) {
			String s = "ingot";
			switch(entry.getOreType()) {
			case DUST:
				s = "dust";
				break;
			case GEM:
			case GEM_ORELESS:
				s = "gem";
				break;
			default:
				break;
			}
			Utils.addShapedOreRecipe(Utils.getOreStack("crux", entry, 1), new Object[] {
					"MEM",
					"SDS",
					"MEM",
					'M', "essenceSupremium",
					'E', s+entry.getOreName(),
					'S', CRUX_SPECIALS.get(entry),
					'D', "blockDiamond",
			});
		}
	}

	@Override
	public List<Pair<String, String>> remaps() {
		return Lists.<Pair<String, String>>newArrayList(
				Pair.<String, String>of("seeds", "mysticalSeeds"),
				Pair.<String, String>of("crops", "mysticalCrops")
				);
	}

	public static Object getEssence(int tier) {
		tier = MathHelper.clamp(tier, 1, ADDITIONS_LOADED ? 6 : 5);
		switch(tier) {
		case 1:
			return "essenceInferium";
		case 2:
			return "essencePrudentium";
		case 3:
		default:
			return "essenceIntermedium";
		case 4:
			return "essenceSuperium";
		case 5:
			return "essenceSupremium";
		case 6:
			return "essenceInsanium";
		}
	}

	public static ItemStack getCraftingSeeds(int tier) {
		tier = MathHelper.clamp(tier, 1, ADDITIONS_LOADED ? 6 : 5);
		switch(tier) {
		case 1:
			return new ItemStack(ModItems.itemCrafting, 1, 17);
		case 2:
			return new ItemStack(ModItems.itemCrafting, 1, 18);
		case 3:
		default:
			return new ItemStack(ModItems.itemCrafting, 1, 19);
		case 4:
			return new ItemStack(ModItems.itemCrafting, 1, 20);
		case 5:
			return new ItemStack(ModItems.itemCrafting, 1, 21);
		case 6:
			Item itemInsanium = Item.REGISTRY.getObject(new ResourceLocation("mysticalagradditions", "insanium"));
			return new ItemStack(itemInsanium, 1, 1);
		}
	}

	@SubscribeEvent
	public void onBonemealEvent(BonemealEvent event) {
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		IBlockState state = event.getBlock();
		if(state.getBlock() instanceof BlockCropsBase) {
			BlockCropsBase block = (BlockCropsBase)state.getBlock();
			if(player.getHeldItemMainhand().getItem() == ModItems.itemFertilizedEssence ||
					player.getHeldItemOffhand().getItem() == ModItems.itemFertilizedEssence) {
				if(block.canGrow(world, pos, state, world.isRemote)) {
					if(!world.isRemote) {
						block.grow(world, world.rand, pos, state);
					}
					event.setResult(Result.ALLOW);
				}
			}
			if(player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == ModItems.itemMysticalFertilizer ||
					player.getHeldItemOffhand() != null &&player.getHeldItemOffhand().getItem() == ModItems.itemMysticalFertilizer) {
				if(block.canGrow(world, pos, state, world.isRemote)) {
					if(!world.isRemote) {
						world.setBlockState(pos, block.withAge(block.getMaxAge()), 2);
					}
					event.setResult(Result.ALLOW);
				}
			}
		}
	}
}
