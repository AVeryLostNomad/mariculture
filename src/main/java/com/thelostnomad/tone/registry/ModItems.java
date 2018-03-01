package com.thelostnomad.tone.registry;

import com.thelostnomad.tone.item.ShardOfSentience;
import com.thelostnomad.tone.item.berries.FuncoBerry;
import com.thelostnomad.tone.item.berries.GlutoBerry;
import com.thelostnomad.tone.item.berries.HastoBerry;
import com.thelostnomad.tone.item.berries.RezzoBerry;
import com.thelostnomad.tone.item.tokens.TokenPullAll;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class ModItems {

    public static TokenPullAll tokenPullAll = new TokenPullAll();
    public static HastoBerry hastoBerryItem = new HastoBerry();
    public static GlutoBerry glutoBerryItem = new GlutoBerry();
    public static FuncoBerry funcoBerryItem = new FuncoBerry();
    public static RezzoBerry rezzoBerryItem = new RezzoBerry();
    public static ItemBlock sentientSaplingItem = new ItemBlock(ModBlocks.sentientSapling);
    public static Item shardOfSentience = new ShardOfSentience();

}
