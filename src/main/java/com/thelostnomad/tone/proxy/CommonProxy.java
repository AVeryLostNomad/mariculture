package com.thelostnomad.tone.proxy;

import com.thelostnomad.tone.block.RootBlock;
import com.thelostnomad.tone.block.SentientTreeCore;
import com.thelostnomad.tone.block.tileentity.TESentientTreeCore;
import com.thelostnomad.tone.registry.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent e) {
        // Each of your tile entities needs to be registered with a name that is unique to your mod.
        GameRegistry.registerTileEntity(TESentientTreeCore.class, TESentientTreeCore.NAME);
    }

    public void init(FMLInitializationEvent e) {
    }

    public void postInit(FMLPostInitializationEvent e) {
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new RootBlock());
        event.getRegistry().register(new SentientTreeCore());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(ModBlocks.rootsBlock).setRegistryName(ModBlocks.rootsBlock.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.sentientTreeCore).setRegistryName(ModBlocks.sentientTreeCore.getRegistryName()));
    }

    @SubscribeEvent
    public static void furnaceBurn(FurnaceFuelBurnTimeEvent event){
        ItemStack burning = event.getItemStack();

    }
}