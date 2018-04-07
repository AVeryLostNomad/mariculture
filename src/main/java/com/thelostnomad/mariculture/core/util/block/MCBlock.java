package com.thelostnomad.mariculture.core.util.block;

import com.thelostnomad.mariculture.core.util.MCRegistry;
import com.thelostnomad.mariculture.core.util.item.MCItem;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.thelostnomad.mariculture.Mariculture.MODID;

public interface MCBlock<T extends Block> extends MCRegistry {
    /** Register this block **/
    default T registerBlock(RegistryEvent.Register<Block> event, String name) {
        Block block = (Block) this;
        block.setUnlocalizedName(name.replace("_", "."));
        block.setRegistryName(new ResourceLocation(MODID, name));
        event.getRegistry().register(block);
        return (T) this;
    }

    default T registerItem(RegistryEvent.Register<Item> event, String name){
        Block block = (Block) this;
        block.setUnlocalizedName(name.replace("_", "."));
        block.setRegistryName(new ResourceLocation(MODID, name));
        getItemBlock().registerItem(event, name);
        return (T) this;
    }

    String getItemStackDisplayName(ItemStack stack);

    /** Return a stack version of this **/
    default ItemStack getStack(int amount) {
        return new ItemStack((T) this, amount);
    }

    /** Return this without any data **/
    default ItemStack getStack() {
        return new ItemStack((T) this);
    }

    /** Get the item block **/
    default MCItem getItemBlock() {
        return new ItemBlockMC(this);
    }

    /** Called by ItemBlockMC to see if the block can be placed**/
    default boolean canPlaceBlockAt(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
        return true;
    }
}