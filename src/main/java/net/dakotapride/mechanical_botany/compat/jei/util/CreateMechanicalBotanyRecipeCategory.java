package net.dakotapride.mechanical_botany.compat.jei.util;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.neoforged.neoforge.fluids.FluidStack;

public abstract class CreateMechanicalBotanyRecipeCategory <T extends StandardProcessingRecipe<?>> extends CreateRecipeCategory<T> {
    public CreateMechanicalBotanyRecipeCategory(Info<T> info) {
        super(info);
    }

    public static IRecipeSlotBuilder addFluidSlot(IRecipeLayoutBuilder builder, int x, int y, FluidIngredient ingredient, RecipeIngredientRole role) {
        int amount = ingredient.getRequiredAmount();
        return builder.addSlot(role, x, y)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(NeoForgeTypes.FLUID_STACK, ingredient.getMatchingFluidStacks())
                .setFluidRenderer(amount, false, 16, 16);
    }

    public static IRecipeSlotBuilder addFluidSlot(IRecipeLayoutBuilder builder, int x, int y, FluidStack stack, RecipeIngredientRole role) {
        return builder.addSlot(role, x, y)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredient(NeoForgeTypes.FLUID_STACK, stack)
                .setFluidRenderer(stack.getAmount(), false, 16, 16);
    }
}
