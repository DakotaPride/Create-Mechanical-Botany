package net.dakotapride.mechanical_botany.kinetics.composter;

import com.simibubi.create.content.kinetics.crusher.AbstractCrushingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.dakotapride.mechanical_botany.ModRecipeTypes;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CompostingRecipe extends AbstractCrushingRecipe {
    public CompostingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(ModRecipeTypes.COMPOSTING, params);
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level worldIn) {
        if (inv.isEmpty())
            return false;
        return ingredients.get(0).test(inv.getItem(0));
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }
}
