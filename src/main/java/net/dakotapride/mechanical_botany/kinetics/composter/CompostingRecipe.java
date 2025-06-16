package net.dakotapride.mechanical_botany.kinetics.composter;

import com.simibubi.create.content.kinetics.crusher.AbstractCrushingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeParams;
import com.simibubi.create.content.processing.recipe.StandardProcessingRecipe;
import net.dakotapride.mechanical_botany.ModRecipeTypes;
import net.dakotapride.mechanical_botany.kinetics.insolator.InsolatingRecipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CompostingRecipe extends AbstractCrushingRecipe {
    public CompostingRecipe(ProcessingRecipeParams params) {
        super(ModRecipeTypes.COMPOSTING, params);
    }

    @Override
    public boolean matches(RecipeInput inv, Level worldIn) {
        if (inv.isEmpty())
            return false;
        return ingredients.get(0).test(inv.getItem(0));
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }


    public static class Serializer extends AbstractCrushingRecipe.Serializer<CompostingRecipe> {
        public Serializer() {
            super(CompostingRecipe::new);
        }
    }
}
