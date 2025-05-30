package net.dakotapride.mechanical_botany;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

//@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class ModCreativeModeTabs {
    private static final DeferredRegister<CreativeModeTab> REGISTER =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateMechanicalBotany.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MECHANICAL_BOTANY = REGISTER.register("mechanical_botany",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.mechanical_botany.base"))
                    .icon(ModBlocks.MECHANICAL_INSOLATOR::asStack)
                    .displayItems(new DisplayItemsGenerator(true, ModCreativeModeTabs.MECHANICAL_BOTANY))
                    .build());


    public static class DisplayItemsGenerator implements CreativeModeTab.DisplayItemsGenerator {

        private final boolean addItems;
        private final DeferredHolder<CreativeModeTab, CreativeModeTab> tabFilter;

        public DisplayItemsGenerator(boolean addItems, DeferredHolder<CreativeModeTab, CreativeModeTab> tabFilter) {
            this.addItems = addItems;
            this.tabFilter = tabFilter;
        }

        @Override
        public void accept(CreativeModeTab.@NotNull ItemDisplayParameters parameters, CreativeModeTab.@NotNull Output output) {
            output.accept(ModBlocks.MECHANICAL_INSOLATOR.asStack());
            output.accept(ModBlocks.MECHANICAL_COMPOSTER.asStack());
            output.accept(ModItems.COMPOST.asStack());
            output.accept(ModFluids.COMPOST.get().getBucket());
            output.accept(ModFluids.MOLTEN_COMPOST.get().getBucket());
            output.accept(ModFluids.VOID_COMPOST.get().getBucket());
        }
    }

    @ApiStatus.Internal
    public static void register(IEventBus bus) {
        REGISTER.register(bus);
    }
}
