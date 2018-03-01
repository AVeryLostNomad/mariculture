package com.thelostnomad.tone.registry;

import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import com.thelostnomad.tone.block.*;
import com.thelostnomad.tone.block.berries.*;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

    @GameRegistry.ObjectHolder("thingsofnaturalenergies:roots")
    public static RootBlock rootsBlock;

    @GameRegistry.ObjectHolder("thingsofnaturalenergies:sentient_core")
    public static SentientTreeCore sentientTreeCore;

    @GameRegistry.ObjectHolder("thingsofnaturalenergies:puller")
    public static BlockPuller blockPuller;

    @GameRegistry.ObjectHolder("thingsofnaturalenergies:pusher")
    public static BlockPusher blockPusher;

    @GameRegistry.ObjectHolder("thingsofnaturalenergies:storagehollow_basic")
    public static BasicStorageHollow storageHollowBasic;

    @GameRegistry.ObjectHolder("thingsofnaturalenergies:sentient_leaves")
    public static SentientLeaves sentientLeaves;

    @GameRegistry.ObjectHolder("thingsofnaturalenergies:sentient_log")
    public static SentientLog sentientLog;

    @GameRegistry.ObjectHolder("thingsofnaturalenergies:sentient_sapling")
    public static SentientSapling sentientSapling;

    // Berries
    @GameRegistry.ObjectHolder("thingsofnaturalenergies:berry_hasto")
    public static HastoBerry hastoBerry;
    @GameRegistry.ObjectHolder("thingsofnaturalenergies:berry_gluto")
    public static GlutoBerry glutoBerry;
    @GameRegistry.ObjectHolder("thingsofnaturalenergies:berry_funco")
    public static FuncoBerry funcoBerry;
    @GameRegistry.ObjectHolder("thingsofnaturalenergies:berry_rezzo")
    public static RezzoBerry rezzoBerry;

    public static BlockBerry getBerryForBiome(Biome b){
        BlockBerry[] toCheck = new BlockBerry[]{hastoBerry, glutoBerry, funcoBerry, rezzoBerry};

        for(BlockBerry berry : toCheck){
            for(Biome biome : berry.getThrivesIn()){
                if (biome.getBiomeName().equals(b.getBiomeName())){
                    return berry;
                }
            }
        }
        return null;
    }

}
