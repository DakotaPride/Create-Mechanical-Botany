package net.dakotapride.mechanical_botany.kinetics.insolator;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import net.dakotapride.mechanical_botany.ModConfigs;
import net.dakotapride.mechanical_botany.ModRecipeTypes;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class InsolatingRecipe extends StandardProcessingRecipe<RecipeInput> {
    public InsolatingRecipe(ProcessingRecipeParams params) {
        super(ModRecipeTypes.INSOLATING, params);
    }

    public SizedFluidIngredient getRequiredFluid() {
//        if (fluidIngredients.isEmpty())
//            throw new IllegalStateException("Insolator Recipe: " + id.toString() + " has no fluid ingredient!");
        return fluidIngredients.getFirst();
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
    public boolean matches(RecipeInput inv, Level worldIn) {
        if (inv.isEmpty())
            return false;

//        return blockEntity.check(inv, worldIn, ingredients, fluidIngredients);
        return ingredients.get(0).test(inv.getItem(0)) && fluidIngredients.get(0).test(getRequiredFluid().getFluids()[0]);
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }

    public static class Serializer extends StandardProcessingRecipe.Serializer<InsolatingRecipe> {
        public Serializer() {
            super(InsolatingRecipe::new);
        }
    }
}
