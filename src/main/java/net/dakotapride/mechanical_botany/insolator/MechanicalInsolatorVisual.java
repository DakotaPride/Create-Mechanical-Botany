package net.dakotapride.mechanical_botany.insolator;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.core.Direction;

public class MechanicalInsolatorVisual extends SingleAxisRotatingVisual<MechanicalInsolatorBlockEntity> implements SimpleDynamicVisual {

    private final MechanicalInsolatorBlockEntity insolator;

    public MechanicalInsolatorVisual(VisualizationContext context, MechanicalInsolatorBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Models.partial(AllPartialModels.MECHANICAL_PUMP_COG, Direction.UP));
        this.insolator = blockEntity;

        animate(partialTick);
    }

    @Override
    public void beginFrame(Context ctx) {
        animate(ctx.partialTick());
    }

    private void animate(float pt) {}
}
