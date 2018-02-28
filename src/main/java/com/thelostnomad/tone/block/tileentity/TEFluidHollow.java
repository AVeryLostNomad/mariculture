package com.thelostnomad.tone.block.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

import javax.annotation.Nullable;

public class TEFluidHollow extends TileEntity implements IFluidTank{

    public static final String NAME = "tone_fluidhollow_tileentity";
    private BlockPos coreLocation = null;

    public FluidTank internalTank;

    private HollowType storageLevel;

    public TEFluidHollow(){}

    public void setStorageLevel(HollowType type){
        this.storageLevel = type;
        init();
    }

    public void init(){
        internalTank = new FluidTank(this.storageLevel.size);
    }

    @Nullable
    @Override
    public FluidStack getFluid() {
        return internalTank.getFluid();
    }

    @Override
    public int getFluidAmount() {
        return internalTank.getFluidAmount();
    }

    @Override
    public int getCapacity() {
        return storageLevel.size;
    }

    @Override
    public FluidTankInfo getInfo() {
        return this.internalTank.getInfo();
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return this.internalTank.fill(resource, doFill);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return this.internalTank.drain(maxDrain, doDrain);
    }

    public enum HollowType {
        INDIVIDUAL(10000, "Specialized"), // Pending balance.
        BASIC(8000, "Basic"),
        BIG(16000, "Big"),
        LARGE(32000, "Large"),
        MASSIVE(42000, "Massive"),
        GARGANTUAN(84000, "Gargantuan"),
        QUITE_BIG(168000, "Quite Big"),
        BIGGER_THAN_THAT(336000, "Even Bigger"),
        SINGULARITY(Integer.MAX_VALUE, "Singularity");

        private int size;
        private String name;

        HollowType(int size, String name) {
            this.size = size;
            this.name = name;
        }

    }
}
