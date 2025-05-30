package net.dakotapride.mechanical_botany;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import net.dakotapride.mechanical_botany.kinetics.composter.MechanicalComposterBlockEntity;
import net.dakotapride.mechanical_botany.kinetics.composter.MechanicalComposterRenderer;
import net.dakotapride.mechanical_botany.kinetics.composter.MechanicalComposterVisual;
import net.dakotapride.mechanical_botany.kinetics.insolator.MechanicalInsolatorBlockEntity;
import net.dakotapride.mechanical_botany.kinetics.insolator.MechanicalInsolatorRenderer;
import net.dakotapride.mechanical_botany.kinetics.insolator.MechanicalInsolatorVisual;

import static net.dakotapride.mechanical_botany.CreateMechanicalBotany.REGISTRATE;

public class ModBlockEntityTypes {

    public static final BlockEntityEntry<MechanicalInsolatorBlockEntity> INSOLATOR = REGISTRATE
            .blockEntity("mechanical_insolator", MechanicalInsolatorBlockEntity::new)
            .visual(() -> MechanicalInsolatorVisual::new)
            .validBlocks(ModBlocks.MECHANICAL_INSOLATOR)
            .renderer(() -> MechanicalInsolatorRenderer::new)
            .register();
    public static final BlockEntityEntry<MechanicalComposterBlockEntity> COMPOSTER = REGISTRATE
            .blockEntity("mechanical_composter", MechanicalComposterBlockEntity::new)
            .visual(() -> MechanicalComposterVisual::new)
            .validBlocks(ModBlocks.MECHANICAL_COMPOSTER)
            .renderer(() -> MechanicalComposterRenderer::new)
            .register();

    public static void register() {}
}
