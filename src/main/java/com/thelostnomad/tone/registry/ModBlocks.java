package com.thelostnomad.tone.registry;

import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import com.thelostnomad.tone.block.*;
import com.thelostnomad.tone.block.berries.BlockBerry;
import com.thelostnomad.tone.block.berries.FuncoBerry;
import com.thelostnomad.tone.block.berries.GlutoBerry;
import com.thelostnomad.tone.block.berries.HastoBerry;
import com.thelostnomad.tone.block.berries.RezzoBerry;
import com.thelostnomad.tone.block.fluid.BlockTransmutationGas;
import com.thelostnomad.tone.block.fluid_hollows.BasicFluidHollow;
import com.thelostnomad.tone.block.storage_hollows.BasicStorageHollow;
import com.thelostnomad.tone.block.storage_hollows.BigStorageHollow;
import javafx.scene.effect.Glow;
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

    @GameRegistry.ObjectHolder("thingsofnaturalenergies:storagehollow_big")
    public static BigStorageHollow storageHollowBig;

    @GameRegistry.ObjectHolder("thingsofnaturalenergies:fluidhollow_basic")
    public static BasicFluidHollow fluidHollowBasic;

    @GameRegistry.ObjectHolder("thingsofnaturalenergies:sentient_leaves")
    public static SentientLeaves sentientLeaves;

    @GameRegistry.ObjectHolder("thingsofnaturalenergies:sentient_log")
    public static SentientLog sentientLog;

    @GameRegistry.ObjectHolder("thingsofnaturalenergies:sentient_sapling")
    public static SentientSapling sentientSapling;

    @GameRegistry.ObjectHolder("thingsofnaturalenergies:transmutation_gas")
    public static BlockTransmutationGas transmutationGas;

    @GameRegistry.ObjectHolder("thingsofnaturalenergies:living_crafting_station")
    public static BlockLivingCraftingStation livingCraftingStation;

    @GameRegistry.ObjectHolder("thingsofnaturalenergies:keeper")
    public static BlockKeeper blockKeeper;

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
        BlockBerry[] toCheck = new BlockBerry[]{hastoBerry, glutoBerry, funcoBerry};

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
