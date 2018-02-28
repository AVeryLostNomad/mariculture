package com.thelostnomad.tone.item.berries;

import com.thelostnomad.tone.ThingsOfNaturalEnergies;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class HastoBerry extends ItemFood {

    public HastoBerry()
    {
        super(1, 0.1f, false);
        this.setMaxStackSize(16);
        this.setCreativeTab(CreativeTabs.MISC);   // the item will appear on the Miscellaneous tab in creative
        setUnlocalizedName(ThingsOfNaturalEnergies.MODID + ".berry_hasto_item");     // Used for localization (en_US.lang)
        setRegistryName("berry_hasto_item");        // The unique name (within your mod) that identifies this block
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;
            entityplayer.addPotionEffect(new PotionEffect(Potion.getPotionById(3), 600, 2));
        }

        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

}
