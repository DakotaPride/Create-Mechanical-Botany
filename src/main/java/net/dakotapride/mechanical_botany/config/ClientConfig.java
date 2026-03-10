package net.dakotapride.mechanical_botany.config;

import net.createmod.catnip.config.ConfigBase;

public class ClientConfig extends ConfigBase {
    public ConfigBool slotRecognition = b(true, "Slot Recognition", Comments.slotRecognition);

    @Override
    public String getName() {
        return "client";
    }

    private static class Comments {
        static String slotRecognition = "Accessibility option for players that want to easily distinguish between input and output items within the goggle hover text.";
    }
}
