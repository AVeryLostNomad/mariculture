package com.thelostnomad.tone.integration.ae2.blocks;

import appeng.api.networking.IGridConnection;
import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import com.thelostnomad.tone.block.tileentity.TELivingCraftingStation;
import com.thelostnomad.tone.block.tileentity.TESentientTreeCore;
import com.thelostnomad.tone.integration.ae2.tileentity.TELivingEnergisticInterface;
import com.thelostnomad.tone.util.ChatUtil;
import com.thelostnomad.tone.util.ITree;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class BlockLivingEnergisticInterface extends Block implements ITree {

    public BlockLivingEnergisticInterface() {
        super(Material.WOOD);
        setUnlocalizedName(ThingsOfNaturalEnergies.MODID + ".living_energistic_interface");     // Used for localization (en_US.lang)
        setRegistryName("living_energistic_interface");
        setCreativeTab(ThingsOfNaturalEnergies.creativeTab);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    // Called when the block is placed or loaded client side to get the tile entity for the block
    // Should return a new instance of the tile entity for the block
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {return new TELivingEnergisticInterface();}

    // Called just after the player places a block.  Start the tileEntity's timer
    // This block in particular should not be "placed" in the conventional sense, but will rather grow naturall in
    // a tree!
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        TELivingEnergisticInterface tei = (TELivingEnergisticInterface) worldIn.getTileEntity(pos);
        tei.setPlacingPlayer((EntityPlayer) placer);
    }

    // the block will render in the SOLID layer.  See http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html for more information.
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.SOLID;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand,
                                    EnumFacing side, float hitX, float hitY, float hitZ) {
        if(worldIn.isRemote) return true;
        TELivingEnergisticInterface tei = (TELivingEnergisticInterface) worldIn.getTileEntity(pos);
        IGridNode node = tei.getNode();
        if(node == null){
            node = tei.refreshNode();
        }
        ItemStack res = tei.drawItem(new ItemStack(Blocks.PISTON, 1));
        tei.getDrivesInNetwork(); // To update the drawing for players, hopefully
        return true;
    }

}
