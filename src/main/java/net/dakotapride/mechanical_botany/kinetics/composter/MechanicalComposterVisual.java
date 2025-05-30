package net.dakotapride.mechanical_botany.kinetics.composter;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;

public class MechanicalComposterVisual extends SingleAxisRotatingVisual<MechanicalComposterBlockEntity> implements SimpleDynamicVisual {

    private final MechanicalComposterBlockEntity composter;

    public MechanicalComposterVisual(VisualizationContext context, MechanicalComposterBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Models.partial(AllPartialModels.COGWHEEL));
        this.composter = blockEntity;

        animate(partialTick);
    }

    @Override
    public void beginFrame(Context ctx) {
        animate(ctx.partialTick());
    }

    private void animate(float pt) {}
}
