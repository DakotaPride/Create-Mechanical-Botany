package net.dakotapride.mechanical_botany.config;

import net.createmod.catnip.config.ConfigBase;

public class ServerConfig extends ConfigBase {
    public final InsolatorProcessingConfig insolator = nested(0, InsolatorProcessingConfig::new,
            "Control certain aspects of the Mechanical Insolator");
    public final ComposterProcessingConfig composter = nested(0, ComposterProcessingConfig::new,
            "Control certain aspects of the Mechanical Composter");

    @Override
    public String getName() {
        return "server";
    }
}