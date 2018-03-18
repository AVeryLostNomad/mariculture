package com.thelostnomad.tone.integration;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IToneIntegration {

    String getIntegrationModid();

    void registerBlocks(RegistryEvent.Register<Block> event);

    void registerItems(RegistryEvent.Register<Item> event);

    void registerTileEntities();

    IBlockState[] getModelBlockStates();

    void preInit(FMLPreInitializationEvent event);
    void init(FMLInitializationEvent event);
    void postInit(FMLPostInitializationEvent event);

}
