package net.dakotapride.mechanical_botany.registry;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.dakotapride.mechanical_botany.CreateMechanicalBotany;
import net.dakotapride.mechanical_botany.kinetics.mechanical_insolator.MechanicalInsolatorBlock;
import net.minecraft.world.level.block.Blocks;

public class CreateMechanicalBotanyBlocks {
    private static final CreateRegistrate REGISTRATE = CreateMechanicalBotany.REGISTRATE;

    public static final BlockEntry<?> MECHANICAL_INSOLATOR = REGISTRATE.block("mechanical_insolator", MechanicalInsolatorBlock::new)
            .simpleItem()
            .initialProperties(() -> Blocks.COPPER_BLOCK).register();

    public static void register() {}

}
