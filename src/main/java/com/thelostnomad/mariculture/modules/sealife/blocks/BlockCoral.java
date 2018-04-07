package com.thelostnomad.mariculture.modules.sealife.blocks;

import com.thelostnomad.mariculture.core.lib.CreativeOrder;
import com.thelostnomad.mariculture.core.util.block.BlockAquatic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public class BlockCoral extends BlockAquatic<BlockCoral.Coral, BlockCoral> {

    public BlockCoral() {
        super(Coral.class);
    }

    @Override
    public int getSortValue(ItemStack stack) {
        return CreativeOrder.CORAL;
    }

    public enum Coral implements IStringSerializable {
        BLUE, CYAN, GREEN, ORANGE, PINK, RED;

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

}
