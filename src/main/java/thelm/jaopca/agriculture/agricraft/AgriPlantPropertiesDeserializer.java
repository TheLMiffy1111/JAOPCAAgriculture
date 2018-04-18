package thelm.jaopca.agriculture.agricraft;

import java.util.function.Function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import thelm.jaopca.api.EnumOreType;
import thelm.jaopca.api.utils.JsonUtils;

public class AgriPlantPropertiesDeserializer {
	
	public static AgriPlantProperties parseAgriPlantPpt(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
		AgriPlantProperties ppt = new AgriPlantProperties();
		return ppt;
	}
	
	public static Function<EnumOreType, String> parseStringFunc(JsonObject object, String memberName, Function<EnumOreType, String> fallback) throws JsonParseException {
		if(JsonUtils.hasField(object, memberName)) {
			JsonElement element = object.get(memberName);
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
		}
		return fallback;
	}
}
