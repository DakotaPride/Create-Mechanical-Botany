package net.dakotapride.mechanical_botany.registry;

import net.dakotapride.mechanical_botany.CreateMechanicalBotany;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class CreateMechanicalBotanyCreativeModeTab {
    private static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateMechanicalBotany.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MECHANICAL_BOTANY = REGISTER.register("mechanical_botany",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.mechanical_botany.base"))
                    //.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                    .icon(Items.WHEAT_SEEDS::getDefaultInstance)
                    .displayItems(new RegistrateDisplayItemsGenerator())
                    .build());

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }

    private static class RegistrateDisplayItemsGenerator implements CreativeModeTab.DisplayItemsGenerator {
        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
            output.accept(CreateMechanicalBotanyBlocks.MECHANICAL_INSOLATOR.asItem());
        }
    }
}
