package net.dakotapride.mechanical_botany.compat.jei;

import com.google.common.base.Preconditions;
import com.simibubi.create.compat.jei.*;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.item.ItemHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.dakotapride.mechanical_botany.CreateMechanicalBotany;
import net.dakotapride.mechanical_botany.ModBlocks;
import net.dakotapride.mechanical_botany.ModItems;
import net.dakotapride.mechanical_botany.ModRecipeTypes;
import net.dakotapride.mechanical_botany.kinetics.composter.CompostingRecipe;
import net.dakotapride.mechanical_botany.kinetics.insolator.InsolatingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static net.createmod.catnip.lang.LangBuilder.resolveBuilders;

@JeiPlugin
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class CreateMechanicalBotanyJEI implements IModPlugin {
    private static final ResourceLocation MOD_ID = ResourceLocation.fromNamespaceAndPath(CreateMechanicalBotany.MOD_ID, "jei_plugin");

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return MOD_ID;
    }

    private final List<IRecipeCategory<?>> categories = new ArrayList<>();

    private void loadCategories(IRecipeCategoryRegistration registration) {
        categories.clear();

        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        List<IRecipeCategory<?>> localCategories = new ArrayList<>();


        localCategories.add(InsolatingCategory.create(guiHelper));
        localCategories.add(CompostingCategory.create(guiHelper));


        registration.addRecipeCategories(localCategories.toArray(new IRecipeCategory[0]));

        this.categories.clear();
        this.categories.addAll(localCategories);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        for (IRecipeCategory<?> category : categories) {
            if (category instanceof CreateRecipeCategory) {
                ((CreateRecipeCategory<?>) category).registerRecipes(registration);
            }
        }
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        loadCategories(registration);
        registration.addRecipeCategories(categories.toArray(IRecipeCategory[]::new));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {

        for (IRecipeCategory<?> category : categories) {
            if (category instanceof CreateRecipeCategory) {
                ((CreateRecipeCategory<?>) category).registerCatalysts(registration);
            }
        }

        //registration.addRecipeCatalyst(ModBlocks.MECHANICAL_COMPOSTER, RecipeTypes.COMPOSTING);
    }

    public static RecipeManager getRecipeManager() {
        if (FMLLoader.getDist() != Dist.CLIENT)
            throw new IllegalStateException();
        var minecraft = Minecraft.getInstance();
        Preconditions.checkNotNull(minecraft);
        var level = minecraft.level;
        Preconditions.checkNotNull(level);
        return level.getRecipeManager();
    }
}