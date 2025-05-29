package net.dakotapride.mechanical_botany.recipe;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import net.dakotapride.mechanical_botany.ModRecipeTypes;

public class CustomProcessingSerializer<T extends ProcessingRecipe<?>> extends ProcessingRecipeSerializer<T> {

    public final MapCodec<T> CODEC = ModRecipeTypes.CODEC.dispatchMap(t ->
            (ModRecipeTypes) t.getTypeInfo(), ModRecipeTypes::processingCodec);


    public CustomProcessingSerializer(ProcessingRecipeBuilder.ProcessingRecipeFactory<T> factory) {
        super(factory);
    }


    @Override
    public MapCodec<T> codec() {
        return CODEC;
    }
}
