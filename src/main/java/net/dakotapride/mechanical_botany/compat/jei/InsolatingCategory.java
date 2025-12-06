package net.dakotapride.mechanical_botany.compat.jei;


import com.simibubi.create.compat.jei.EmptyBackground;
import com.simibubi.create.compat.jei.ItemIcon;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.utility.CreateLang;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.createmod.catnip.lang.LangBuilder;
import net.dakotapride.mechanical_botany.CreateMechanicalBotany;
import net.dakotapride.mechanical_botany.ModBlocks;
import net.dakotapride.mechanical_botany.ModRecipeTypes;
import net.dakotapride.mechanical_botany.compat.jei.util.CreateMechanicalBotanyRecipeCategory;
import net.dakotapride.mechanical_botany.kinetics.insolator.InsolatingRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class InsolatingCategory extends CreateMechanicalBotanyRecipeCategory<InsolatingRecipe> {

    private final AnimatedMechanicalInsolator mechanicalInsolator = new AnimatedMechanicalInsolator();

    public InsolatingCategory(Info<InsolatingRecipe> info) {
        super(info);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, InsolatingRecipe recipe, IFocusGroup focuses) {
        IRecipeSlotBuilder plantInputSlot = builder.addSlot(RecipeIngredientRole.INPUT, 5, 9)
                .setBackground(getRenderedSlot(), -1, -1)
                .addIngredients(recipe.getIngredients().get(0));

        addFluidSlot(builder, 25, 9, recipe.getRequiredFluid());

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

        if (recipe.consumeItem())
            plantInputSlot.addTooltipCallback((recipeSlotView, tooltip) -> tooltip.add(1, translateDirect("text.insolating.input_consumed").withStyle(ChatFormatting.BOLD)));
    }

    public static final RecipeType<RecipeHolder<InsolatingRecipe>> TYPE =
            RecipeType.createRecipeHolderType(CreateMechanicalBotany.asResource("insolating"));

    public static InsolatingCategory create(IGuiHelper guiHelper) {
        Component title = Component.translatable("recipe.mechanical_botany.insolating");
        IDrawable background = new EmptyBackground(178, 72);
        IDrawable icon = new ItemIcon(() -> new ItemStack(ModBlocks.MECHANICAL_INSOLATOR.get()));
        Supplier<ItemStack> catalystStackSupplier = ModBlocks.MECHANICAL_INSOLATOR::asStack;
        Info<InsolatingRecipe> info = new Info<>(
                TYPE,
                title,
                background,
                icon,
                InsolatingCategory::getAllRecipes,
                List.of(catalystStackSupplier)
        );
        return new InsolatingCategory(info);
    }

    private static List<RecipeHolder<InsolatingRecipe>> getAllRecipes() {
        return CreateMechanicalBotanyJEI.getRecipeManager().getAllRecipesFor(ModRecipeTypes.INSOLATING.getType());
    }

    @Override
    public void draw(InsolatingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_ARROW.render(graphics, 85, 41);
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 45, 20);
        mechanicalInsolator.draw(graphics, 38, 27);
        graphics.drawString(Minecraft.getInstance().font, translateDirect("text.processing_ticks", (recipe.getProcessingDuration() / 20)), 2, 60, 0xffffff);
    }

    public static MutableComponent translateDirect(String key, Object... args) {
        Object[] args1 = LangBuilder.resolveBuilders(args);
        return Component.translatable(CreateMechanicalBotany.MOD_ID + "." + key, args1);
    }

}
