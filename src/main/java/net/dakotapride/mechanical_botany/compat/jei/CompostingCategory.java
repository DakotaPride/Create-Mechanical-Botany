package net.dakotapride.mechanical_botany.compat.jei;


import com.simibubi.create.compat.jei.DoubleItemIcon;
import com.simibubi.create.compat.jei.EmptyBackground;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.dakotapride.mechanical_botany.CreateMechanicalBotany;
import net.dakotapride.mechanical_botany.ModBlocks;
import net.dakotapride.mechanical_botany.ModItems;
import net.dakotapride.mechanical_botany.ModRecipeTypes;
import net.dakotapride.mechanical_botany.kinetics.composter.CompostingRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class CompostingCategory extends CreateRecipeCategory<CompostingRecipe> {

    private final AnimatedMechanicalComposter mechanicalComposter = new AnimatedMechanicalComposter();

    public CompostingCategory(Info<CompostingRecipe> info) {
        super(info);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CompostingRecipe recipe, IFocusGroup focuses) {
        builder
                .addSlot(RecipeIngredientRole.INPUT, 5, 9)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(recipe.getIngredients().get(0));

        List<ProcessingOutput> results = recipe.getRollableResults();
        boolean single = results.size() == 1;
        int i = 0;
        for (ProcessingOutput output : results) {
            int xOffset = i % 2 == 0 ? 0 : 19;
            int yOffset = (i / 2) * -19;

            builder
                    .addSlot(RecipeIngredientRole.OUTPUT, single ? 139 : 133 + xOffset, 37 + yOffset)
                    .setBackground(getRenderedSlot(output), -1, -1)
                    .addItemStack(output.getStack())
                    .addRichTooltipCallback(addStochasticTooltip(output));

            i++;
        }
    }

    public static final RecipeType<CompostingRecipe> TYPE =
            RecipeType.create(CreateMechanicalBotany.MOD_ID, "composting", CompostingRecipe.class);

    public static CompostingCategory create(IGuiHelper guiHelper) {
        Component title = Component.translatable("recipe.mechanical_botany.composting");
        IDrawable background = new EmptyBackground(178, 72);
        IDrawable icon = new DoubleItemIcon(() -> new ItemStack(ModBlocks.MECHANICAL_COMPOSTER.get()), () -> ModItems.COMPOST.asStack());
        Supplier<ItemStack> catalystStackSupplier = ModBlocks.MECHANICAL_COMPOSTER::asStack;
        Info<CompostingRecipe> info = new Info<>(
                TYPE,
                title,
                background,
                icon,
                CompostingCategory::getAllRecipes,
                List.of(catalystStackSupplier)
        );
        return new CompostingCategory(info);
    }

    private static List<CompostingRecipe> getAllRecipes() {
        return CreateMechanicalBotanyJEI.getRecipeManager().getAllRecipesFor(ModRecipeTypes.COMPOSTING.getType());
    }

    @Override
    public void draw(CompostingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_ARROW.render(graphics, 85, 41);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 45, 20);
        mechanicalComposter.draw(graphics, 38, 27);
    }

}
