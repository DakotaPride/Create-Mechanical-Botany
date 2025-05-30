package net.dakotapride.mechanical_botany;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.dakotapride.mechanical_botany.kinetics.composter.ComposterPonderScenes;
import net.dakotapride.mechanical_botany.kinetics.insolator.InsolatorPonderScenes;
import net.minecraft.resources.ResourceLocation;

public class ModPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(ModBlocks.MECHANICAL_INSOLATOR).addStoryBoard("insolator", new InsolatorPonderScenes.Intro());
        HELPER.forComponents(ModBlocks.MECHANICAL_INSOLATOR).addStoryBoard("compost_use", new InsolatorPonderScenes.CompostUsage());
        HELPER.forComponents(ModBlocks.MECHANICAL_INSOLATOR).addStoryBoard("molten_compost_use", new InsolatorPonderScenes.MoltenCompostUsage());
        HELPER.forComponents(ModBlocks.MECHANICAL_INSOLATOR).addStoryBoard("void_compost_use", new InsolatorPonderScenes.VoidCompostUsage());

        HELPER.forComponents(ModBlocks.MECHANICAL_COMPOSTER).addStoryBoard("composter", new ComposterPonderScenes.Intro());
        HELPER.forComponents(ModBlocks.MECHANICAL_COMPOSTER).addStoryBoard("layered_composter", new ComposterPonderScenes.LayeredComposters());
        HELPER.forComponents(ModBlocks.MECHANICAL_COMPOSTER).addStoryBoard("compost_creation", new ComposterPonderScenes.CreatingLiquidCompost());
        HELPER.forComponents(ModBlocks.MECHANICAL_COMPOSTER).addStoryBoard("compost_use", new InsolatorPonderScenes.CompostUsage());
        HELPER.forComponents(ModBlocks.MECHANICAL_COMPOSTER).addStoryBoard("molten_compost_use", new InsolatorPonderScenes.MoltenCompostUsage());
        HELPER.forComponents(ModBlocks.MECHANICAL_COMPOSTER).addStoryBoard("void_compost_use", new InsolatorPonderScenes.VoidCompostUsage());
    }
}
