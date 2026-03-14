package net.dakotapride.mechanical_botany.kinetics.insolator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import net.dakotapride.mechanical_botany.ModConfigs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class InsolatingProcessingRecipeParams extends ProcessingRecipeParams {
    public static MapCodec<InsolatingProcessingRecipeParams> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            codec(InsolatingProcessingRecipeParams::new).forGetter(Function.identity()),
            Codec.BOOL.optionalFieldOf("consume_input", false).forGetter(InsolatingProcessingRecipeParams::consumeInput)
    ).apply(instance, (params, keepHeldItem) -> {
        params.consumeInput = keepHeldItem;
        return params;
    }));
    public static StreamCodec<RegistryFriendlyByteBuf, InsolatingProcessingRecipeParams> STREAM_CODEC = streamCodec(InsolatingProcessingRecipeParams::new);

    protected boolean consumeInput;

    protected final boolean consumeInput() {
        if (!ModConfigs.server().insolator.readConsumeInputFlag.get())
            return false;
        return consumeInput;
    }

    @Override
    protected void encode(RegistryFriendlyByteBuf buffer) {
        super.encode(buffer);
        ByteBufCodecs.BOOL.encode(buffer, consumeInput);
    }

    @Override
    protected void decode(RegistryFriendlyByteBuf buffer) {
        super.decode(buffer);
        consumeInput = ByteBufCodecs.BOOL.decode(buffer);
    }
}
