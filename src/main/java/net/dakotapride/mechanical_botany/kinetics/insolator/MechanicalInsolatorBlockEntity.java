package net.dakotapride.mechanical_botany.kinetics.insolator;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.sound.SoundScapes;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.lang.LangBuilder;
import net.dakotapride.mechanical_botany.CreateMechanicalBotany;
import net.dakotapride.mechanical_botany.ModBlockEntityTypes;
import net.dakotapride.mechanical_botany.ModRecipeTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class MechanicalInsolatorBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {

    public ItemStackHandler inputInv;
    public ItemStackHandler outputInv;
    public IItemHandler capability;
    public int timer;
    private InsolatingRecipe lastRecipe;

    public SmartFluidTankBehaviour tank;

    public MechanicalInsolatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inputInv = new ItemStackHandler(1);
        outputInv = new ItemStackHandler(9);
        capability = new InsolatorInventoryHandler();
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        //IFluidHandler handler = level.getCapability(Capabilities.FluidHandler.BLOCK, this.getBlockPos(), null);

        LangBuilder mb = CreateLang.translate("generic.unit.millibuckets");
        CreateLang.translate("gui.goggles.fluid_container").forGoggles(tooltip);

//        tooltip.add(Component.literal(getCurrentFluidInTank().toString()));
        if (!getCurrentFluidInTank().isEmpty()) {
            CreateLang.fluidName(getCurrentFluidInTank())
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 1);
            CreateLang.builder()
                    .add(CreateLang.number(getCurrentFluidInTank().getAmount())
                            .add(mb)
                            .style(ChatFormatting.GOLD))
                    .text(ChatFormatting.GRAY, " / ")
                    .add(CreateLang.number(tank.getCapability().getTankCapacity(0))
                            .add(mb)
                            .style(ChatFormatting.DARK_GRAY))
                    .forGoggles(tooltip, 1);
        } else {
            CreateLang.translate("gui.goggles.fluid_container.capacity")
                    .add(CreateLang.number(tank.getCapability().getTankCapacity(0))
                            .add(mb)
                            .style(ChatFormatting.GOLD))
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 1);
        }

        CreateLang.translate("gui.goggles.item_container").forGoggles(tooltip);
        if (!inputInv.getStackInSlot(0).isEmpty()) {
            CreateLang.itemName(inputInv.getStackInSlot(0))
                    .style(ChatFormatting.GRAY)
                    .forGoggles(tooltip, 1);
            CreateLang.builder().add(CreateLang.number(inputInv.getStackInSlot(0).getCount())
                    .style(ChatFormatting.GOLD))
                    .forGoggles(tooltip, 1);
        } else {
            translate("text.insolator.empty").style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
        }

        //addBlankSpace(tooltip);

        return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }

    public static LangBuilder builder() {
        return new LangBuilder(CreateMechanicalBotany.MOD_ID);
    }

    public static LangBuilder translate(String langKey, Object... args) {
        return builder().translate(langKey, args);
    }

    private static void addBlankSpace(List<Component> tooltip) {
        tooltip.add(Component.literal(""));
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntityTypes.INSOLATOR.get(),
                (be, context) -> {
                    if (context == Direction.DOWN)
                        return be.tank.getCapability();
                    return null;
                }
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntityTypes.INSOLATOR.get(),
                (be, context) -> be.capability
        );
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this));
        behaviours.add(tank = SmartFluidTankBehaviour.single(this, 4000)
                .allowExtraction().allowInsertion());
        super.addBehaviours(behaviours);
        // registerAwardables(behaviours, AllAdvancements.MILLSTONE);
    }

    public FluidStack getCurrentFluidInTank() {
        return tank.getPrimaryHandler().getFluid();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void tickAudio() {
        super.tickAudio();

        if (getSpeed() == 0)
            return;
        if (inputInv.getStackInSlot(0)
                .isEmpty() || tank.isEmpty())
            return;

        float pitch = Mth.clamp((Math.abs(getSpeed()) / 256f) + .45f, .85f, 1f);
        SoundScapes.play(SoundScapes.AmbienceGroup.COG, worldPosition, pitch);
    }

    @Override
    public void tick() {
        super.tick();
        FluidStack currentFluidInTank = getCurrentFluidInTank();

        if (getSpeed() == 0)
            return;
        for (int i = 0; i < outputInv.getSlots(); i++)
            if (outputInv.getStackInSlot(i).getCount() == outputInv.getSlotLimit(i))
                return;

        if (timer > 0) {
            timer -= getProcessingSpeed();

            if (timer <= 0)
                process();
            return;
        }

        if (inputInv.getStackInSlot(0)
                .isEmpty() || tank.isEmpty())
            return;

        RecipeWrapper inventoryIn = new RecipeWrapper(inputInv);
        if (lastRecipe == null || !lastRecipe.matches(inventoryIn, level)) {
            Optional<RecipeHolder<InsolatingRecipe>> recipe = ModRecipeTypes.INSOLATING.find(inventoryIn, level);
            if (!recipe.isPresent()) {
                timer = 100;
                sendData();
            } else {
                lastRecipe = recipe.get().value();
                timer = lastRecipe.getProcessingDuration();
                sendData();
            }
            return;
        }

        timer = lastRecipe.getProcessingDuration();
        sendData();
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inputInv);
        ItemHelper.dropContents(level, worldPosition, outputInv);
    }

    public boolean check(RecipeInput inv, Level worldIn, NonNullList<Ingredient> ingredients, NonNullList<FluidIngredient> fluidIngredients) {
        if (inv.isEmpty())
            return false;
        if (tank.isEmpty())
            return false;

        return ingredients.get(0).test(inv.getItem(0)) && fluidIngredients.get(0).test(getCurrentFluidInTank());
    }

    private void process() {
        RecipeWrapper inventoryIn = new RecipeWrapper(inputInv);

        if (lastRecipe == null || !lastRecipe.matches(inventoryIn, level) || !lastRecipe.getRequiredFluid().test(getCurrentFluidInTank())) {
            Optional<RecipeHolder<InsolatingRecipe>> recipe = ModRecipeTypes.INSOLATING.find(inventoryIn, level);
            if (!recipe.isPresent())
                return;
            lastRecipe = recipe.get().value();
        }

        ItemStack stackInSlot = inputInv.getStackInSlot(0);
        FluidStack fluidInSlot = getCurrentFluidInTank();
        if (lastRecipe.getIngredients().get(0).test(stackInSlot) && lastRecipe.getFluidIngredients().get(0).test(fluidInSlot)) {
            stackInSlot.shrink(1);
            fluidInSlot.shrink(lastRecipe.getRequiredFluid().getRequiredAmount());
            inputInv.setStackInSlot(0, stackInSlot);
            lastRecipe.rollResults().forEach(stack -> ItemHandlerHelper.insertItemStacked(outputInv, stack, false));
            sendData();
            setChanged();
        }
        // award(AllAdvancements.MILLSTONE);
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.putInt("Timer", timer);
        compound.put("InputInventory", inputInv.serializeNBT(registries));
        compound.put("OutputInventory", outputInv.serializeNBT(registries));
        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        timer = compound.getInt("Timer");
        inputInv.deserializeNBT(registries, compound.getCompound("InputInventory"));
        outputInv.deserializeNBT(registries, compound.getCompound("OutputInventory"));
        super.read(compound, registries, clientPacket);
    }

    public int getProcessingSpeed() {
        return Mth.clamp((int) Math.abs(getSpeed() / 16f), 1, 512);
    }

//    @Override
//    public <T> @NotNull LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
//        if (isItemHandlerCap(cap))
//            return capability.cast();
//        return super.getCapability(cap, side);
//    }

    private boolean canProcess(ItemStack stack) {
        ItemStackHandler tester = new ItemStackHandler(1);
        tester.setStackInSlot(0, stack);
        RecipeWrapper inventoryIn = new RecipeWrapper(tester);

        if (lastRecipe != null && lastRecipe.matches(inventoryIn, level) && lastRecipe.getRequiredFluid().test(getCurrentFluidInTank()))
            return true;

        return ModRecipeTypes.INSOLATING.find(inventoryIn, level).isPresent();
    }

    private class InsolatorInventoryHandler extends CombinedInvWrapper {

        public InsolatorInventoryHandler() {
            super(inputInv, outputInv);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (outputInv == getHandlerFromIndex(getIndexForSlot(slot)))
                return false;
            return canProcess(stack) && super.isItemValid(slot, stack);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (outputInv == getHandlerFromIndex(getIndexForSlot(slot)))
                return stack;
            if (!isItemValid(slot, stack))
                return stack;
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (inputInv == getHandlerFromIndex(getIndexForSlot(slot)))
                return ItemStack.EMPTY;
            return super.extractItem(slot, amount, simulate);
        }
    }

}
