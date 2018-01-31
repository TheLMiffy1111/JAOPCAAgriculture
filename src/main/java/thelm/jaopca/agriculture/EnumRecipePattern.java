package thelm.jaopca.agriculture;

import java.util.Arrays;

import net.minecraft.item.ItemStack;

public enum EnumRecipePattern {

	HORIZONTAL(3, "EEE",               'E', null),
	CIRCLE    (4, " E ", "E E", " E ", 'E', null),
	CROSS     (5, " E ", "EEE", " E ", 'E', null),
	TOPDOWN   (6, "EEE", "   ", "EEE", 'E', null),
	ANVIL     (7, "EEE", " E ", "EEE", 'E', null),
	BORDER    (8, "EEE", "E E", "EEE", 'E', null),
	FULL      (9, "EEE", "EEE", "EEE", 'E', null);

	public final int amount;
	public final Object[] inputArray;

	EnumRecipePattern(int amount, Object... inputArray) {
		this.amount = amount;
		this.inputArray = inputArray;
	}

	public Object[] getRecipePattern(Object input) {
		Object[] arr = Arrays.<Object>copyOf(inputArray, inputArray.length);
		arr[arr.length-1] = input;
		return arr;
	}

	public static EnumRecipePattern getPattern(int amount, EnumRecipePattern fallback) {
		for(EnumRecipePattern pattern : values()) {
			if(pattern.amount == amount) {
				return pattern;
			}
		}
		return fallback;
	}
}
