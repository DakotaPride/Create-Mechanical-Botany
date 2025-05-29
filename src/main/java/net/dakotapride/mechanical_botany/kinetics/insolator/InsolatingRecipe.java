package net.dakotapride.mechanical_botany.kinetics.insolator;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.dakotapride.mechanical_botany.ModRecipeTypes;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class InsolatingRecipe extends ProcessingRecipe<RecipeInput> {
    public InsolatingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(ModRecipeTypes.INSOLATING, params);
    }

    public FluidIngredient getRequiredFluid() {
        if (fluidIngredients.isEmpty())
            throw new IllegalStateException("Insolator Recipe: " + id.toString() + " has no fluid ingredient!");
        return fluidIngredients.get(0);
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
    public boolean matches(RecipeInput inv, Level worldIn) {
        if (inv.isEmpty())
            return false;

//        return blockEntity.check(inv, worldIn, ingredients, fluidIngredients);
        return ingredients.get(0).test(inv.getItem(0)) && fluidIngredients.get(0).test(getRequiredFluid().getMatchingFluidStacks().get(0));
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }
}
