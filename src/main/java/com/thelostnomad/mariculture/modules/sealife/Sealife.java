package com.thelostnomad.mariculture.modules.sealife;

import com.thelostnomad.mariculture.Mariculture;
import com.thelostnomad.mariculture.core.util.annotation.MCLoader;
import com.thelostnomad.mariculture.modules.sealife.blocks.BlockCoral;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@MCLoader
public class Sealife {
    public static final BlockCoral CORAL = new BlockCoral();

    public static void preInit(){
        Mariculture.logger.error("Loading sealife");
    }

    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        Mariculture.logger.error("Registering coral block");
        CORAL.registerBlock(event, "coral");
    }

    public static void registerItems(RegistryEvent.Register<Item> event) {
        Mariculture.logger.error("Registering coral block item");
        CORAL.registerItem(event, "coral");
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerAllModels(final ModelRegistryEvent event) {
        Mariculture.logger.error("Registering coral block model");
        CORAL.registerModels(CORAL.getItemBlock().getStack().getItem());
    }

}
