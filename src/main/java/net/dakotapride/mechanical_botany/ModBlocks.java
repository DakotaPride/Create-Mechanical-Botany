package net.dakotapride.mechanical_botany;

import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.dakotapride.mechanical_botany.insolator.MechanicalInsolatorBlock;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;

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

    public static void register() {
        // load the class and register everything
        //GarnishedStoneAutomation.LOGGER.info("Registering blocks for " + GarnishedStoneAutomation.NAME);

        BlockStressValues.IMPACTS.registerProvider((block) -> {
            if (block == MECHANICAL_INSOLATOR.get()) return () -> 16.0D;
            else return null;
        });
    }

}
