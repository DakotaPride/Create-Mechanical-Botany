package net.dakotapride.mechanical_botany;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.dakotapride.mechanical_botany.insolator.MechanicalInsolatorBlockEntity;
import net.dakotapride.mechanical_botany.insolator.MechanicalInsolatorRenderer;
import net.dakotapride.mechanical_botany.insolator.MechanicalInsolatorVisual;

import static net.dakotapride.mechanical_botany.CreateMechanicalBotany.REGISTRATE;

public class ModBlockEntityTypes {

    public static final BlockEntityEntry<MechanicalInsolatorBlockEntity> INSOLATOR = REGISTRATE
            .blockEntity("mechanical_insolator", MechanicalInsolatorBlockEntity::new)
            .visual(() -> MechanicalInsolatorVisual::new)
            .validBlocks(ModBlocks.MECHANICAL_INSOLATOR)
            .renderer(() -> MechanicalInsolatorRenderer::new)
            .register();

    public static void register() {}
}
