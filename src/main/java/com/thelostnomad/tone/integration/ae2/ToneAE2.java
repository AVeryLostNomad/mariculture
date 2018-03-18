package com.thelostnomad.tone.integration.ae2;

import appeng.api.AEApi;
import com.thelostnomad.tone.block.BlockLivingCraftingStation;
import com.thelostnomad.tone.block.BlockPuller;
import com.thelostnomad.tone.block.tileentity.TELivingCraftingStation;
import com.thelostnomad.tone.integration.IToneIntegration;
import com.thelostnomad.tone.integration.ae2.blocks.BlockLivingEnergisticInterface;
import com.thelostnomad.tone.integration.ae2.tileentity.TELivingEnergisticInterface;
import com.thelostnomad.tone.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ToneAE2 implements IToneIntegration {

    @GameRegistry.ObjectHolder("thingsofnaturalenergies:living_energistic_interface")
    public static BlockLivingEnergisticInterface livingEnergisticInterface;

    @Override
    public String getIntegrationModid() {
        return "appliedenergistics2";
    }

    @Override
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new BlockLivingEnergisticInterface());
    }

    @Override
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(livingEnergisticInterface).setRegistryName(livingEnergisticInterface.getRegistryName()));
    }

    @Override
    public void registerTileEntities() {
        GameRegistry.registerTileEntity(TELivingEnergisticInterface.class, TELivingEnergisticInterface.NAME);
    }

    @Override
    public IBlockState[] getModelBlockStates() {
        return new IBlockState[]{
                livingEnergisticInterface.getDefaultState()
        };
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
    }

}
