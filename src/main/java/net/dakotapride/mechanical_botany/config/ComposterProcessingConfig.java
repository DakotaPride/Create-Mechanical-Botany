package net.dakotapride.mechanical_botany.config;

import net.createmod.catnip.config.ConfigBase;

public class ComposterProcessingConfig extends ConfigBase {
    public ConfigBase.ConfigInt processingTimeMultiplier = i(1, 1, "Processing Time Multiplier", Comments.processingTimeMultiplier);

    @Override
    public String getName() {
        return "composter";
    }

    private static class Comments {
        static String processingTimeMultiplier = "Controls the multiplier of a given processing recipe.";
    }
}