package com.thelostnomad.tone.block.berries;

import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import com.thelostnomad.tone.block.tileentity.TESentientTreeCore;
import com.thelostnomad.tone.block.tileentity.TEStorageHollow;
import com.thelostnomad.tone.registry.ModBlocks;
import com.thelostnomad.tone.registry.ModItems;
import com.thelostnomad.tone.util.ChatUtil;
import com.thelostnomad.tone.util.IBerry;
import com.thelostnomad.tone.util.ITree;
import com.thelostnomad.tone.util.TreeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeBeach;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HastoBerry extends BlockBerry {

    // Hastoberries increase a sentient tree's tickrate. Trees start at the default
    public HastoBerry() {
        toSpawnOnBreak = ModItems.hastoBerryItem;
        breakString = "hastoberry";
        setUnlocalizedName(ThingsOfNaturalEnergies.MODID + ".berry_hasto");     // Used for localization (en_US.lang)
        setRegistryName("berry_hasto");
        setCreativeTab(ThingsOfNaturalEnergies.creativeTab);
    }

    @Override
    public Biome[] getThrivesIn() {
        return new Biome[]{Biomes.DESERT, Biomes.DESERT_HILLS}; // Hastoberries are only obtainable in the desert
    }

}
