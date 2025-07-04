package net.dakotapride.mechanical_botany.kinetics.insolator;

import com.simibubi.create.foundation.item.SmartInventory;
import net.dakotapride.mechanical_botany.kinetics.composter.MechanicalComposterBlockEntity;

public class MechanicalInsolatorInventory extends SmartInventory {
    public MechanicalInsolatorInventory(int slots, MechanicalInsolatorBlockEntity be) {
        super(slots, be, 64, true);
    }

}
