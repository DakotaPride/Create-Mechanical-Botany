package net.dakotapride.mechanical_botany;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.fluids.VirtualFluid;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.util.entry.FluidEntry;
import net.createmod.catnip.theme.Color;
import net.dakotapride.mechanical_botany.compat.jei.CreateMechanicalBotanyJEI;
import net.dakotapride.mechanical_botany.fluid.BloodFluid;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import org.joml.Vector3f;

import java.util.function.Supplier;

import static net.dakotapride.mechanical_botany.CreateMechanicalBotany.REGISTRATE;

public class ModFluids {

    public static final FluidEntry<BaseFlowingFluid.Flowing> COMPOST =
            REGISTRATE.standardFluid("compost",
                            SolidRenderedPlaceableFluidType.create(0x523C18,
                                    () -> 1f / 8f * AllConfigs.client().honeyTransparencyMultiplier.getF()))
                    .properties(b -> b.viscosity(2000)
                            .density(1400))
                    .fluidProperties(p -> p.levelDecreasePerBlock(2)
                            .tickRate(25)
                            .slopeFindDistance(3)
                            .explosionResistance(100f))
                    .source(BaseFlowingFluid.Source::new) // TODO: remove when Registrate fixes FluidBuilder
                    .bucket()
                    .build()
                    .register();
    public static final FluidEntry<BaseFlowingFluid.Flowing> MOLTEN_COMPOST =
            REGISTRATE.standardFluid("molten_compost",
                            SolidRenderedPlaceableFluidType.create(0x442814,
                                    () -> 1f / 8f * AllConfigs.client().honeyTransparencyMultiplier.getF()))
                    .properties(b -> b.viscosity(2000)
                            .density(1400))
                    .fluidProperties(p -> p.levelDecreasePerBlock(2)
                            .tickRate(25)
                            .slopeFindDistance(3)
                            .explosionResistance(100f))
                    .source(BaseFlowingFluid.Source::new) // TODO: remove when Registrate fixes FluidBuilder
                    .bucket()
                    .build()
                    .register();
    public static final FluidEntry<BaseFlowingFluid.Flowing> VOID_COMPOST =
            REGISTRATE.standardFluid("void_compost",
                            SolidRenderedPlaceableFluidType.create(0x432652,
                                    () -> 1f / 8f * AllConfigs.client().honeyTransparencyMultiplier.getF()))
                    .properties(b -> b.viscosity(2000)
                            .density(1400))
                    .fluidProperties(p -> p.levelDecreasePerBlock(2)
                            .tickRate(25)
                            .slopeFindDistance(3)
                            .explosionResistance(100f))
                    .source(BaseFlowingFluid.Source::new) // TODO: remove when Registrate fixes FluidBuilder
                    .bucket()
                    .build()
                    .register();

    public static final FluidEntry<BloodFluid> BLOOD =
            REGISTRATE.virtualFluid("blood", BloodFluid.Type::new, BloodFluid::createSource, BloodFluid::createFlowing).register();

    public static void register() {}

    private static class SolidRenderedPlaceableFluidType extends AllFluids.TintedFluidType {

        private Vector3f fogColor;
        private Supplier<Float> fogDistance;

        public static FluidBuilder.FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance) {
            return (p, s, f) -> {
                SolidRenderedPlaceableFluidType fluidType = new SolidRenderedPlaceableFluidType(p, s, f);
                fluidType.fogColor = new Color(fogColor, false).asVectorF();
                fluidType.fogDistance = fogDistance;
                return fluidType;
            };
        }

        private SolidRenderedPlaceableFluidType(Properties properties, ResourceLocation stillTexture,
                                                ResourceLocation flowingTexture) {
            super(properties, stillTexture, flowingTexture);
        }

        @Override
        protected int getTintColor(FluidStack stack) {
            return NO_TINT;
        }

        /*
         * Removing alpha from tint prevents optifine from forcibly applying biome
         * colors to modded fluids (this workaround only works for fluids in the solid
         * render layer)
         */
        @Override
        public int getTintColor(FluidState state, BlockAndTintGetter world, BlockPos pos) {
            return 0x00ffffff;
        }

        @Override
        protected Vector3f getCustomFogColor() {
            return fogColor;
        }

        @Override
        protected float getFogDistanceModifier() {
            return fogDistance.get();
        }

    }
}
