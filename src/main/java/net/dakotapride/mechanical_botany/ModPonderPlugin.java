package net.dakotapride.mechanical_botany;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class ModPonderPlugin implements PonderPlugin {
    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        //PonderPlugin.super.registerScenes(helper);
        ModPonderScenes.register(helper);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        ModPonderTags.register(helper);
    }

    @Override
    public String getModId() {
        return CreateMechanicalBotany.MOD_ID;
    }
}
