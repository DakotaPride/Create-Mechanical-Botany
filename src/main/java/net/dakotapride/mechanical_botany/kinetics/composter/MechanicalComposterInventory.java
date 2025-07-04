package net.dakotapride.mechanical_botany.kinetics.composter;

import com.simibubi.create.foundation.item.SmartInventory;
import net.dakotapride.mechanical_botany.kinetics.insolator.MechanicalInsolatorBlockEntity;

public class MechanicalComposterInventory extends SmartInventory {


    public MechanicalComposterInventory(int slots, MechanicalComposterBlockEntity be) {
        super(slots, be, 64, true);
    }

}
