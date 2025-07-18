package net.dakotapride.mechanical_botany;

import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.dakotapride.mechanical_botany.kinetics.composter.MechanicalComposterBlock;
import net.dakotapride.mechanical_botany.kinetics.insolator.MechanicalInsolatorBlock;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;
import static net.dakotapride.mechanical_botany.CreateMechanicalBotany.REGISTRATE;

public class ModBlocks {
    public static final BlockEntry<MechanicalInsolatorBlock> MECHANICAL_INSOLATOR =
            REGISTRATE.block("mechanical_insolator", MechanicalInsolatorBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion().requiresCorrectToolForDrops())
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
                    //.transform(CStress.setImpact(8.0D))
                    .item()
                    .transform(customItemModel())
                    // .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .register();
    public static final BlockEntry<MechanicalComposterBlock> MECHANICAL_COMPOSTER =
            REGISTRATE.block("mechanical_composter", MechanicalComposterBlock::new)
                    .initialProperties(SharedProperties::copperMetal)
                    .properties(p -> p.noOcclusion().requiresCorrectToolForDrops())
                    .transform(pickaxeOnly())
                    .blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
                    //.transform(CStress.setImpact(8.0D))
                    .item()
                    .transform(customItemModel())
                    // .tag(AllTags.AllBlockTags.SAFE_NBT.tag)
                    .register();

    public static void register() {
        // load the class and register everything
        //GarnishedStoneAutomation.LOGGER.info("Registering blocks for " + GarnishedStoneAutomation.NAME);

        BlockStressValues.IMPACTS.registerProvider((block) -> {
            if (block == MECHANICAL_INSOLATOR.get()) return () -> (double) ModConfigs.server().insolator.kineticStressImpact.get();
            if (block == MECHANICAL_COMPOSTER.get()) return () -> (double) ModConfigs.server().composter.kineticStressImpact.get();
            else return null;
        });
    }

}
