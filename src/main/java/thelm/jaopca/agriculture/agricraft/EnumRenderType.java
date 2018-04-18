package thelm.jaopca.agriculture.agricraft;

public enum EnumRenderType {
	CROSS, HASH;

	public static EnumRenderType fromName(String name) {
		for(EnumRenderType type : EnumRenderType.values()) {
			if(type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return EnumRenderType.CROSS;
	}
}
