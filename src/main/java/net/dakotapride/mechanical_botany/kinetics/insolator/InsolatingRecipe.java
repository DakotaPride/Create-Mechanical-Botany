package net.dakotapride.mechanical_botany.kinetics.insolator;

import ca.weblite.objc.Proxy;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import net.dakotapride.mechanical_botany.CreateMechanicalBotany;
import net.dakotapride.mechanical_botany.ModConfigs;
import net.dakotapride.mechanical_botany.ModRecipeTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class InsolatingRecipe extends ProcessingRecipe<RecipeWrapper, InsolatingProcessingRecipeParams> {
    private Supplier<ItemStack> forcedResult;
    public InsolatingRecipe(InsolatingProcessingRecipeParams params) {
        super(ModRecipeTypes.INSOLATING, params);
    }

    public SizedFluidIngredient getRequiredFluid() {
//        if (fluidIngredients.isEmpty())
//            throw new IllegalStateException("Insolator Recipe: " + id.toString() + " has no fluid ingredient!");
        return fluidIngredients.getFirst();
    }

    // Holy bandaid fix batman
    @Override
    public List<ItemStack> rollResults(RandomSource randomSource) {
        List<ItemStack> results = new ArrayList<>();
        for (int i = 0; i < this.getRollableResults().size(); i++) {
            ProcessingOutput output = this.getRollableResults().get(i);
            ItemStack stack = i == 0 && forcedResult != null ? forcedResult.get() : output.rollOutput(randomSource);
            if (!this.consumeItem() && i == 0)
                stack.setCount(stack.getCount() - 1);
            if (!stack.isEmpty())
                results.add(stack);
        }
        return results;
    }

    public void enforceNextResult(Supplier<ItemStack> stack) {
        forcedResult = stack;
    }

    public boolean consumeItem() {
        return params.consumeInput();
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxFluidInputCount() {
        return 1;
    }

    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }

    @Override
    public int getProcessingDuration() {
        return super.getProcessingDuration() * ModConfigs.server().insolator.processingTimeMultiplier.get();
    }

    //    @Override
//    public int getProcessingDuration() {
//        return super.getProcessingDuration() * 2;
//    }

    @Override
    public boolean matches(RecipeWrapper inv, Level worldIn) {
        if (inv.isEmpty())
            return false;

//        return blockEntity.check(inv, worldIn, ingredients, fluidIngredients);
        return ingredients.get(0).test(inv.getItem(0)) && fluidIngredients.get(0).test(getRequiredFluid().getFluids()[0]);
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }

    public static class Serializer<R extends InsolatingRecipe> implements RecipeSerializer<R> {
        private final MapCodec<R> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, R> streamCodec;

        public Serializer(Factory<InsolatingProcessingRecipeParams, R> factory) {
            this.codec = ProcessingRecipe.codec(factory, InsolatingProcessingRecipeParams.CODEC);
            this.streamCodec = ProcessingRecipe.streamCodec(factory, InsolatingProcessingRecipeParams.STREAM_CODEC);
        }

        @Override
        public MapCodec<R> codec() {
            return codec;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, R> streamCodec() {
            return streamCodec;
        }
    }
}
