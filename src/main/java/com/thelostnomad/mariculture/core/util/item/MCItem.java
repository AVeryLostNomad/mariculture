package com.thelostnomad.mariculture.core.util.item;

import com.thelostnomad.mariculture.core.util.MCRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import static com.thelostnomad.mariculture.Mariculture.MODID;

public interface MCItem<T extends Item> extends MCRegistry {

    default T registerItem(RegistryEvent.Register<Item> event, String name){
        Item item =  (Item) this;
        item.setUnlocalizedName(name.replace("_", "."));
        item.setRegistryName(new ResourceLocation(MODID, name));
        event.getRegistry().register(item);
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            registerModels(item);
        }
        return (T) this;
    }

    /** Return a stack version of this **/
    default ItemStack getStack(int amount) {
        return new ItemStack((T) this, 1, amount);
    }

    /** Return this without any data **/
    default ItemStack getStack() {
        return new ItemStack((T) this);
    }
}