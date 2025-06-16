package net.dakotapride.mechanical_botany.recipe;

import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MechancialBotanyRecipeTypeInfo<R extends Recipe<?>> implements IRecipeTypeInfo {

    private final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<R>> serializer;
    private final DeferredHolder<RecipeType<?>, RecipeType<R>> type;

    public MechancialBotanyRecipeTypeInfo(String name, Supplier<? extends RecipeSerializer<R>> serializerSupplier,
                                   DeferredRegister<RecipeSerializer<?>> serializerRegister,
                                   DeferredRegister<RecipeType<?>> typeRegister) {
        this.serializer = serializerRegister.register(name, serializerSupplier);
        this.type = typeRegister.register(name, () -> RecipeType.simple(this.serializer.getId()));
    }

    @Override
    public ResourceLocation getId() {
        return serializer.getId();
    }

    @Override
    public RecipeSerializer<R> getSerializer() {
        return serializer.get();
    }

    @Override
    public RecipeType<R> getType() {
        return type.get();
    }
}