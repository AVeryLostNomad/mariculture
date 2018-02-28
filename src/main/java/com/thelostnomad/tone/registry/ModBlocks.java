package com.thelostnomad.tone.registry;

import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import com.thelostnomad.tone.block.*;
import com.thelostnomad.tone.block.berries.BlockBerry;
import com.thelostnomad.tone.block.berries.FuncoBerry;
import com.thelostnomad.tone.block.berries.GlutoBerry;
import com.thelostnomad.tone.block.berries.HastoBerry;
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

    public static BlockBerry getBerryForBiome(Biome b){
        BlockBerry[] toCheck = new BlockBerry[]{hastoBerry, glutoBerry, funcoBerry};

        for(BlockBerry berry : toCheck){
            if(berry instanceof GlutoBerry){
                ThingsOfNaturalEnergies.logger.error("Checking gluto");
            }
            for(Biome biome : berry.getThrivesIn()){
                ThingsOfNaturalEnergies.logger.error("Biome: " + biome.getBiomeName() + " and " + b.getBiomeName());
                if (biome.getBiomeName().equals(b.getBiomeName())){
                    return berry;
                }
            }
        }
        return null;
    }

}
