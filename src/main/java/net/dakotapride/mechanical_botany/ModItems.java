package net.dakotapride.mechanical_botany;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;

import static net.dakotapride.mechanical_botany.CreateMechanicalBotany.REGISTRATE;

public class ModItems {
    public static ItemEntry<?> COMPOST = REGISTRATE.item("compost", BoneMealItem::new).register();

    public static void register() {}
}
