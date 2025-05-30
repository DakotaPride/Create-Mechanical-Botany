package net.dakotapride.mechanical_botany;

import com.simibubi.create.Create;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class ModPonderTags {
    //public static final ResourceLocation KINETIC_RELAYS = loc("kinetic_relays");

    private static ResourceLocation loc(String id) {
        return Create.asResource(id);
    }

    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

//        helper.registerTag(KINETIC_RELAYS)
//                .addToIndex()
//                .item(AllBlocks.COGWHEEL.get(), true, false)
//                .title("Kinetic Blocks")
//                .description("Components which help relaying Rotational Force elsewhere")
//                .register();

        HELPER.addToTag(AllCreatePonderTags.KINETIC_APPLIANCES).add(ModBlocks.MECHANICAL_INSOLATOR).add(ModBlocks.MECHANICAL_COMPOSTER);

        HELPER.addToTag(AllCreatePonderTags.ARM_TARGETS).add(ModBlocks.MECHANICAL_INSOLATOR).add(ModBlocks.MECHANICAL_COMPOSTER);
    }
}
