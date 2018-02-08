package com.thelostnomad.tone.registry;

import com.thelostnomad.tone.block.RootBlock;
import com.thelostnomad.tone.block.SentientTreeCore;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

    @GameRegistry.ObjectHolder("thingsofnaturalenergies:roots")
    public static RootBlock rootsBlock;

    @GameRegistry.ObjectHolder("thingsofnaturalenergies:sentient_core")
    public static SentientTreeCore sentientTreeCore;
}
