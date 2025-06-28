package net.dakotapride.mechanical_botany.fluid;

import com.simibubi.create.AllFluids;
import com.simibubi.create.content.fluids.VirtualFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.FluidStack;

public class BloodFluid extends VirtualFluid {

    public BloodFluid(Properties properties, boolean source) {
        super(properties, source);
    }

    public static BloodFluid createSource(Properties properties) {
        return new BloodFluid(properties, true);
    }

    public static BloodFluid createFlowing(Properties properties) {
        return new BloodFluid(properties, false);
    }

    public static class Type extends AllFluids.TintedFluidType {
        public Type(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture) {
            super(properties, stillTexture, flowingTexture);
        }

        @Override
        protected int getTintColor(FluidStack stack) {
            return 0xC62135 | 0xff000000;
        }

        @Override
        protected int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
            return NO_TINT;
        }
    }
}
