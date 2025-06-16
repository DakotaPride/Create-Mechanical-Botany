package net.dakotapride.mechanical_botany;

import net.dakotapride.mechanical_botany.kinetics.composter.CompostingRecipe;
import net.dakotapride.mechanical_botany.kinetics.insolator.InsolatingRecipe;
import net.dakotapride.mechanical_botany.recipe.MechancialBotanyRecipeTypeInfo;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;
import java.util.function.Supplier;

public class ModRecipeTypes {


    public static final MechancialBotanyRecipeTypeInfo<InsolatingRecipe> INSOLATING =
            register("insolating", InsolatingRecipe.Serializer::new);
    public static final MechancialBotanyRecipeTypeInfo<CompostingRecipe> COMPOSTING =
            register("composting", CompostingRecipe.Serializer::new);

    public static void register(IEventBus modEventBus) {
        ShapedRecipePattern.setCraftingSize(9, 9);
        Registers.SERIALIZER_REGISTER.register(modEventBus);
        Registers.TYPE_REGISTER.register(modEventBus);
    }

    public static <I extends RecipeInput, R extends Recipe<I>> Optional<RecipeHolder<R>> find(I inv, Level world, MechancialBotanyRecipeTypeInfo<R> recipe) {
        return world.getRecipeManager().getRecipeFor(recipe.getType(), inv, world);
    }

    private static <R extends Recipe<?>> MechancialBotanyRecipeTypeInfo<R> register(String name, Supplier<? extends RecipeSerializer<R>> serializerSupplier) {
        return new MechancialBotanyRecipeTypeInfo<>(name, serializerSupplier, Registers.SERIALIZER_REGISTER, Registers.TYPE_REGISTER);
    }

    private static class Registers {
        private static final DeferredRegister<RecipeSerializer<?>> SERIALIZER_REGISTER = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, CreateMechanicalBotany.MOD_ID);
        private static final DeferredRegister<RecipeType<?>> TYPE_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, CreateMechanicalBotany.MOD_ID);
    }
}
