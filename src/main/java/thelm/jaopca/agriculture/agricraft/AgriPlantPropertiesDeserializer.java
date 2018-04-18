package thelm.jaopca.agriculture.agricraft;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import thelm.jaopca.agriculture.agricraft.AgriPlantProperties.Condition;
import thelm.jaopca.agriculture.agricraft.AgriPlantProperties.Product;
import thelm.jaopca.api.EnumOreType;
import thelm.jaopca.api.IOreEntry;
import thelm.jaopca.api.utils.JsonUtils;

public class AgriPlantPropertiesDeserializer {

	public static final Type DOUBLE_FUNCTION_TYPE = new TypeToken<ToDoubleFunction<IOreEntry>>(){}.getType();
	public static final Type INT_FUNCTION_TYPE = new TypeToken<ToIntFunction<IOreEntry>>(){}.getType();

	public static AgriPlantProperties parseAgriPlantPpt(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
		List<Function<EnumOreType, String>> seedFormatFuncs = Lists.<Function<EnumOreType, String>>newArrayList();
		if(JsonUtils.isJsonArray(json, "seed_formats")) {
			JsonArray arr = JsonUtils.getJsonArray(json, "seed_formats");
			for(JsonElement element : arr) {
				Function<EnumOreType, String> seedFormatFunc = parseStringFunc(element, "seed_format", null);
				if(seedFormatFunc != null) {
					seedFormatFuncs.add(seedFormatFunc);
				}
			}
		}
		ToDoubleFunction<IOreEntry> growthChanceFunc = JsonUtils.deserializeClass(json, "growth_chance", entry->0.9D, context, DOUBLE_FUNCTION_TYPE);
		ToDoubleFunction<IOreEntry> growthBonusFunc = JsonUtils.deserializeClass(json, "growth_bonus", entry->0.025D, context, DOUBLE_FUNCTION_TYPE);
		boolean fertilizable = JsonUtils.getBoolean(json, "fertilizable", false);
		boolean weed = JsonUtils.getBoolean(json, "weed", false);
		boolean aggressive = JsonUtils.getBoolean(json, "aggressive", false);
		ToDoubleFunction<IOreEntry> spreadChanceFunc = JsonUtils.deserializeClass(json, "spread_chance", entry->0.1D, context, DOUBLE_FUNCTION_TYPE);
		ToDoubleFunction<IOreEntry> spawnChanceFunc = JsonUtils.deserializeClass(json, "spawn_chance", entry->0D, context, DOUBLE_FUNCTION_TYPE);
		ToDoubleFunction<IOreEntry> grassDropChanceFunc = JsonUtils.deserializeClass(json, "grass_drop_chance", entry->0D, context, DOUBLE_FUNCTION_TYPE);
		ToDoubleFunction<IOreEntry> seedDropChanceFunc = JsonUtils.deserializeClass(json, "seed_drop_chance", entry->0D, context, DOUBLE_FUNCTION_TYPE);
		ToDoubleFunction<IOreEntry> seedDropBonusFunc = JsonUtils.deserializeClass(json, "seed_drop_bonus", entry->0D, context, DOUBLE_FUNCTION_TYPE);
		List<Product> products = Lists.<Product>newArrayList();
		if(JsonUtils.isJsonArray(json, "products")) {
			JsonArray arr = JsonUtils.getJsonArray(json, "products");
			for(JsonElement element : arr) {
				if(element.isJsonObject()) {
					JsonObject obj = element.getAsJsonObject();
					Function<EnumOreType, String> formatFunc = parseStringFunc(obj, "format", null);
					if(formatFunc != null) {
						ToIntFunction<IOreEntry> minFunc = JsonUtils.deserializeClass(obj, "min", entry->5, context, INT_FUNCTION_TYPE);
						ToIntFunction<IOreEntry> rangeFunc = JsonUtils.deserializeClass(obj, "range", entry->0, context, INT_FUNCTION_TYPE);
						ToDoubleFunction<IOreEntry> chanceFunc = JsonUtils.deserializeClass(obj, "chance", entry->0.99D, context, DOUBLE_FUNCTION_TYPE);
						Product product = new Product();
						product.
						setMinFunc(minFunc).
						setRangeFunc(rangeFunc).
						setChanceFunc(chanceFunc).
						setFormatFunc(formatFunc);
						products.add(product);
					}
				}
			}
		}
		int minLight = JsonUtils.getInt(json, "min_light", 10);
		int maxLight = JsonUtils.getInt(json, "max_light", 16);
		List<String> soils = Lists.<String>newArrayList();
		if(JsonUtils.isJsonArray(json, "soils")) {
			JsonArray arr = JsonUtils.getJsonArray(json, "soils");
			for(JsonElement element : arr) {
				if(JsonUtils.isString(element)) {
					soils.add(JsonUtils.getString(element, "soil"));
				}
			}
		}
		List<Condition> conditions = Lists.<Condition>newArrayList();
		if(JsonUtils.isJsonArray(json, "conditions")) {
			JsonArray arr = JsonUtils.getJsonArray(json, "conditions");
			for(JsonElement element : arr) {
				if(element.isJsonObject()) {
					JsonObject obj = element.getAsJsonObject();
					Function<EnumOreType, String> formatFunc = parseStringFunc(obj, "format", null);
					if(formatFunc != null) {
						ToIntFunction<IOreEntry> amountFunc = JsonUtils.deserializeClass(obj, "amount", entry->1, context, INT_FUNCTION_TYPE);
						int minX = JsonUtils.getInt(obj, "min_x", 0);
						int minY = JsonUtils.getInt(obj, "min_y", -2);
						int minZ = JsonUtils.getInt(obj, "min_z", 0);
						int maxX = JsonUtils.getInt(obj, "max_x", minX);
						int maxY = JsonUtils.getInt(obj, "max_y", minY);
						int maxZ = JsonUtils.getInt(obj, "max_z", minZ);
						Condition condition = new Condition();
						condition.
						setAmountFunc(amountFunc).
						setRange(minX, minY, minZ, maxX, maxY, maxZ).
						setFormatFunc(formatFunc);
						conditions.add(condition);
					}
				}
			}
		}
		EnumRenderType renderType = EnumRenderType.fromName(JsonUtils.getString(json, "render_type", "hash"));
		List<List<Pair<String, Boolean>>> textures = Lists.<List<Pair<String, Boolean>>>newArrayList();
		if(JsonUtils.isJsonArray(json, "textures")) {
			JsonArray arr = JsonUtils.getJsonArray(json, "textures");
			for(JsonElement ele : arr) {
				if(ele.isJsonArray()) {
					JsonArray array = ele.getAsJsonArray();
					List<Pair<String, Boolean>> texture = Lists.<Pair<String, Boolean>>newArrayList();
					for(JsonElement element : array) {
						if(element.isJsonObject()) {
							JsonObject obj = element.getAsJsonObject();
							String location = JsonUtils.getString(obj, "location", null);
							if(location != null) {
								boolean isTinted = JsonUtils.getBoolean(obj, "is_tinted", false);
								texture.add(Pair.<String, Boolean>of(location, isTinted));
							}
						}
					}
					textures.add(texture);
				}
			}
		}
		AgriPlantProperties ppt = new AgriPlantProperties();
		ppt.
		setSeedFormatFuncs(seedFormatFuncs).
		setGrowthChanceFunc(growthChanceFunc).
		setGrowthBonusFunc(growthBonusFunc).
		setFertilizable(fertilizable).
		setWeed(weed).
		setAggressive(aggressive).
		setSpreadChanceFunc(spreadChanceFunc).
		setSpawnChanceFunc(spawnChanceFunc).
		setGrassDropChanceFunc(grassDropChanceFunc).
		setSeedDropChanceFunc(seedDropChanceFunc).
		setSeedDropBonusFunc(seedDropBonusFunc).
		setProducts(products).
		setMinLight(minLight).
		setMaxLight(maxLight).
		setSoils(soils).
		setConditions(conditions).
		setRenderType(renderType).
		setTextures(textures);
		return ppt;
	}

	public static Function<EnumOreType, String> parseStringFunc(JsonObject object, String memberName, Function<EnumOreType, String> fallback) throws JsonParseException {
		if(JsonUtils.hasField(object, memberName)) {
			JsonElement element = object.get(memberName);
			return parseStringFunc(element, memberName, fallback);
		}
		return fallback;
	}

	public static Function<EnumOreType, String> parseStringFunc(JsonElement element, String memberName, Function<EnumOreType, String> fallback) throws JsonParseException {
		if(JsonUtils.isString(element)) {
			String str = element.getAsString();
			return type->str;
		}
		else if(element.isJsonObject()) {
			JsonObject json = element.getAsJsonObject();
			String ingot = JsonUtils.getString(json, "ingot", "");
			String gem = JsonUtils.getString(json, "gem", "");
			String dust = JsonUtils.getString(json, "dust", "");
			String ingot_oreless = JsonUtils.getString(json, "ingot_oreless", ingot);
			String gem_oreless = JsonUtils.getString(json, "gem_oreless", gem);
			return type->{
				switch(type) {
				default:
				case INGOT:
					return ingot;
				case GEM:
					return gem;
				case DUST:
					return dust;
				case INGOT_ORELESS:
					return ingot_oreless;
				case GEM_ORELESS:
					return gem_oreless;
				}
			};
		}
		return fallback;
	}
}
