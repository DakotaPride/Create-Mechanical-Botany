package net.dakotapride.mechanical_botany.compat.jei.util;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Arrays;

public abstract class CreateMechanicalBotanyRecipeCategory<T extends Recipe<?>> extends CreateRecipeCategory<T> {
    public CreateMechanicalBotanyRecipeCategory(Info<T> info) {
        super(info);
    }

    public static IRecipeSlotBuilder addFluidSlot(IRecipeLayoutBuilder builder, int x, int y, SizedFluidIngredient ingredient) {
        int amount = ingredient.amount();
        return builder.addSlot(RecipeIngredientRole.INPUT, x, y)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.asList(ingredient.getFluids()))
                .setFluidRenderer(amount, false, 16, 16);
    }

    public static IRecipeSlotBuilder addFluidSlot(IRecipeLayoutBuilder builder, int x, int y, FluidStack stack, RecipeIngredientRole role) {
        return builder.addSlot(role, x, y)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredient(NeoForgeTypes.FLUID_STACK, stack)
                .setFluidRenderer(stack.getAmount(), false, 16, 16);
    }
}
