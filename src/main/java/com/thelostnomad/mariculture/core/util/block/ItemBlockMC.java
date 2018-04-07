package com.thelostnomad.mariculture.core.util.block;

import com.thelostnomad.mariculture.core.util.item.MCItem;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockMC extends ItemBlock implements MCItem {
    private final MCBlock mcBlock;

    public ItemBlockMC(MCBlock block) {
        super((Block) block);
        this.mcBlock = block;
        setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return mcBlock.getItemStackDisplayName(stack);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(facing);
        }

        if (player.getHeldItem(hand).getCount() != 0 && player.canPlayerEdit(pos, facing, player.getHeldItem(hand)) &&
                mcBlock.canPlaceBlockAt(player, player.getHeldItem(hand), worldIn, pos, facing)) {

            int i = this.getMetadata(player.getHeldItem(hand).getMetadata());
            this.block.onBlockPlacedBy(worldIn, pos, iblockstate, player, player.getHeldItem(hand));
            if (placeBlockAt(player.getHeldItem(hand), player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate)) {
                SoundType soundtype = this.block.getSoundType();
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                player.getHeldItem(hand).setCount(player.getHeldItem(hand).getCount() - 1);
            }

            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return mcBlock.getSortValue(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels(Item item) {
        mcBlock.registerModels(item);
    }
}