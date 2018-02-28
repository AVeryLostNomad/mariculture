package com.thelostnomad.tone.item.berries;

import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class GlutoBerry extends ItemFood {

    public GlutoBerry()
    {
        super(1, 10.0f, false);
        this.setMaxStackSize(16);
        this.setCreativeTab(ThingsOfNaturalEnergies.creativeTab);   // the item will appear on the Miscellaneous tab in creative
        setUnlocalizedName(ThingsOfNaturalEnergies.MODID + ".berry_gluto_item");     // Used for localization (en_US.lang)
        setRegistryName("berry_gluto_item");        // The unique name (within your mod) that identifies this block
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

}
