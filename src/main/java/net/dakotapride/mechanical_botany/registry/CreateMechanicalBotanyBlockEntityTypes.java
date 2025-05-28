package net.dakotapride.mechanical_botany.registry;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.dakotapride.mechanical_botany.CreateMechanicalBotany;
import net.dakotapride.mechanical_botany.kinetics.mechanical_insolator.MechanicalInsolatorBlockEntity;
import net.dakotapride.mechanical_botany.kinetics.mechanical_insolator.MechanicalInsolatorRenderer;

public class CreateMechanicalBotanyBlockEntityTypes {
    private static final CreateRegistrate REGISTRATE = CreateMechanicalBotany.REGISTRATE;


    public static final BlockEntityEntry<MechanicalInsolatorBlockEntity> MECHANICAL_INSOLATOR = REGISTRATE
            .blockEntity("mechanical_insolator", MechanicalInsolatorBlockEntity::new)
            .visual(() -> SingleAxisRotatingVisual.of(AllPartialModels.MECHANICAL_PUMP_COG), false)
            .validBlocks(CreateMechanicalBotanyBlocks.MECHANICAL_INSOLATOR)
            .renderer(() -> MechanicalInsolatorRenderer::new)
            .register();

    public static void register() {}
}
