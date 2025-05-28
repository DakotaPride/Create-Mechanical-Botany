package net.dakotapride.mechanical_botany.kinetics.mechanical_insolator;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class MechanicalInsolatorRenderer extends KineticBlockEntityRenderer<MechanicalInsolatorBlockEntity> {

    public MechanicalInsolatorRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected SuperByteBuffer getRotatedModel(MechanicalInsolatorBlockEntity be, BlockState state) {
        return CachedBuffers.partial(AllPartialModels.MECHANICAL_PUMP_COG, state);
    }

}
