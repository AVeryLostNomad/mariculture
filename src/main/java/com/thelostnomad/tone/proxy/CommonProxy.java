package com.thelostnomad.tone.proxy;

import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import com.thelostnomad.tone.block.*;
import com.thelostnomad.tone.block.berries.FuncoBerry;
import com.thelostnomad.tone.block.berries.GlutoBerry;
import com.thelostnomad.tone.block.berries.HastoBerry;
import com.thelostnomad.tone.block.tileentity.TEPuller;
import com.thelostnomad.tone.block.tileentity.TEPusher;
import com.thelostnomad.tone.block.tileentity.TESentientTreeCore;
import com.thelostnomad.tone.block.tileentity.TEStorageHollow;
import com.thelostnomad.tone.registry.ModBlocks;
import com.thelostnomad.tone.registry.ModGuiHandler;
import com.thelostnomad.tone.registry.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {

    public static ItemBlock sentientLog;

    public void preInit(FMLPreInitializationEvent e) {
        // Each of your tile entities needs to be registered with a name that is unique to your mod.
        GameRegistry.registerTileEntity(TESentientTreeCore.class, TESentientTreeCore.NAME);
        GameRegistry.registerTileEntity(TEStorageHollow.class, TEStorageHollow.NAME);
        GameRegistry.registerTileEntity(TEPuller.class, TEPuller.NAME);
        GameRegistry.registerTileEntity(TEPusher.class, TEPusher.NAME);


        NetworkRegistry.INSTANCE.registerGuiHandler(ThingsOfNaturalEnergies.instance, ModGuiHandler.getInstance());


//        blockSimple = (BlockSimple)(new BlockSimple().setUnlocalizedName("mbe01_block_simple_unlocalised_name"));
//        blockSimple.setRegistryName("mbe01_block_simple_registry_name");
//        ForgeRegistries.BLOCKS.register(blockSimple);
//
//        // We also need to create and register an ItemBlock for this block otherwise it won't appear in the inventory
//        itemBlockSimple = new ItemBlock(blockSimple);
//        itemBlockSimple.setRegistryName(blockSimple.getRegistryName());
//        ForgeRegistries.ITEMS.register(itemBlockSimple);
    }

    public void init(FMLInitializationEvent e) {
    }

    public void postInit(FMLPostInitializationEvent e) {
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new RootBlock());
        event.getRegistry().register(new SentientTreeCore());
        event.getRegistry().register(new BasicStorageHollow());
        event.getRegistry().register(new SentientSapling());
        event.getRegistry().register(new SentientLog());
        event.getRegistry().register(new SentientLeaves());
        event.getRegistry().register(new BlockPuller());
        event.getRegistry().register(new BlockPusher());

        //Berries
        event.getRegistry().register(new HastoBerry());
        event.getRegistry().register(new GlutoBerry());
        event.getRegistry().register(new FuncoBerry());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(ModBlocks.rootsBlock).setRegistryName(ModBlocks.rootsBlock.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.sentientTreeCore).setRegistryName(ModBlocks.sentientTreeCore.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.storageHollowBasic).setRegistryName(ModBlocks.storageHollowBasic.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.sentientLeaves).setRegistryName(ModBlocks.sentientLeaves.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.sentientLog).setRegistryName(ModBlocks.sentientLog.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.sentientSapling).setRegistryName(ModBlocks.sentientSapling.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.blockPuller).setRegistryName(ModBlocks.blockPuller.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.blockPusher).setRegistryName(ModBlocks.blockPusher.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.hastoBerry).setRegistryName(ModBlocks.hastoBerry.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.glutoBerry).setRegistryName(ModBlocks.glutoBerry.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.funcoBerry).setRegistryName(ModBlocks.funcoBerry.getRegistryName()));

        event.getRegistry().register(ModItems.tokenPullAll);
        event.getRegistry().register(ModItems.hastoBerryItem);
        event.getRegistry().register(ModItems.glutoBerryItem);
    }

}
