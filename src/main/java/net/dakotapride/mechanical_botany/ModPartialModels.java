package net.dakotapride.mechanical_botany;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class ModPartialModels {
    public static final PartialModel MECHANICAL_PUMP_COG_ROT = block("mechanical_pump_cog_rot_90");

    private static PartialModel block(String path) {
        return PartialModel.of(CreateMechanicalBotany.asResource("block/" + path));
    }

    public static void register() {}
}
