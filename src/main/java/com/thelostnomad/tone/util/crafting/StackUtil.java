package com.thelostnomad.tone.util.crafting;

import net.minecraft.item.ItemStack;

public class StackUtil {

    public static boolean stacksEqual(ItemStack a, ItemStack b){
        ItemStack one = a.copy();
        one.setCount(1);
        ItemStack two = b.copy();
        two.setCount(1);
        return ItemStack.areItemStacksEqual(one, two);
    }

}
