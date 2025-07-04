package net.dakotapride.mechanical_botany.config;

import net.createmod.catnip.config.ConfigBase;

public class InsolatorProcessingConfig extends ConfigBase {
    public ConfigBase.ConfigInt processingTimeMultiplier = i(1, 1, "Processing Time Multiplier", Comments.processingTimeMultiplier);
    public ConfigInt tankSize = i(1000, 1000, 16000, "Tank Size", Comments.tankSize, Comments.requiresReplacement);

    @Override
    public String getName() {
        return "insolator";
    }

    private static class Comments {
        static String requiresReplacement = "Requires you to break and replace the block in order to update the tank size.";
        static String processingTimeMultiplier = "Controls the multiplier of a given processing recipe.";
        static String tankSize = "Controls the tank size of the Mechanical Insolator.";
    }
}