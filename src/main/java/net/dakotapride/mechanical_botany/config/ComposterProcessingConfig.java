package net.dakotapride.mechanical_botany.config;

import net.createmod.catnip.config.ConfigBase;

public class ComposterProcessingConfig extends ConfigBase {
    public ConfigInt processingTimeMultiplier = i(1, 1, "Processing Time Multiplier", Comments.processingTimeMultiplier);
    public ConfigInt kineticStressImpact = i(16, 2, "Kinetic Stress Impact", Comments.kineticStressImpact, Comments.requiresReplacement);

    @Override
    public String getName() {
        return "composter";
    }

    private static class Comments {
        static String requiresReplacement = "Requires you to break and replace the block in order to update blockstate.";
        static String processingTimeMultiplier = "Controls the multiplier of a given processing recipe.";
        static String kineticStressImpact = "The kinetic stress impact value";
    }
}
