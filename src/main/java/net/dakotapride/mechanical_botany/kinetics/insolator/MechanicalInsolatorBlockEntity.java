package net.dakotapride.mechanical_botany.kinetics.insolator;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.CombinedTankWrapper;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.sound.SoundScapes;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.lang.LangBuilder;
import net.dakotapride.mechanical_botany.CreateMechanicalBotany;
import net.dakotapride.mechanical_botany.ModBlockEntityTypes;
import net.dakotapride.mechanical_botany.ModConfigs;
import net.dakotapride.mechanical_botany.ModRecipeTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class MechanicalInsolatorBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {

    public MechanicalInsolatorInventory inputInv;
    public MechanicalInsolatorInventory outputInv;
    public IItemHandlerModifiable itemCapability;
    public int timer;
    private InsolatingRecipe lastRecipe;

    public SmartFluidTankBehaviour tank;
    protected IFluidHandler fluidCapability;

    public MechanicalInsolatorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inputInv = new MechanicalInsolatorInventory(1, this);
        outputInv = new MechanicalInsolatorInventory(9, this);
        itemCapability = new MechanicalInsolatorInvWrapper(inputInv, outputInv);
    }

    private class MechanicalInsolatorInvWrapper extends CombinedInvWrapper {
        public MechanicalInsolatorInvWrapper(SmartInventory input, SmartInventory output) {
            super(input, output);
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (outputInv == getHandlerFromIndex(getIndexForSlot(slot)))
                return false;
            if (stack.is(ItemTags.SAPLINGS))
                return ModConfigs.server().insolator.canProcessSaplings.get();
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

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {

        LangBuilder mb = CreateLang.translate("generic.unit.millibuckets");
        CreateLang.translate("gui.goggles.fluid_container").forGoggles(tooltip);

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
        for (int i = 0; i < itemCapability.getSlots(); i++) {
            ItemStack stackInSlot = itemCapability.getStackInSlot(i);
            if (stackInSlot.isEmpty()) {
                continue;
            }
            CreateLang.text("")
                    .add(Component.translatable(stackInSlot.getDescriptionId())
                            .withStyle(ChatFormatting.GRAY))
                    .add(CreateLang.text(" x" + stackInSlot.getCount())
                            .style(ChatFormatting.GREEN))
                    .forGoggles(tooltip, 1);
        }
        if (inputInv.getStackInSlot(0).isEmpty()) {
            CreateMechanicalBotany.translate("text.cannot_process.empty").style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
            if (!ModConfigs.server().insolator.canProcessSaplings.get())
                CreateMechanicalBotany.translate("text.config.disabled_saplings").style(ChatFormatting.RED).forGoggles(tooltip, 1);
        }

        return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntityTypes.INSOLATOR.get(),
                (be, context) -> {
                    if (context == null || MechanicalInsolatorBlock.hasPipeTowards(context))
                        return be.fluidCapability;
                    else return null;
                }
        );
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntityTypes.INSOLATOR.get(),
                (be, context) -> be.itemCapability
        );
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this));
        tank = new SmartFluidTankBehaviour(SmartFluidTankBehaviour.INPUT, this,
                1, ModConfigs.server().insolator.tankSize.get(), true).allowExtraction().allowInsertion();
        behaviours.add(tank);
        fluidCapability = new CombinedTankWrapper(tank.getCapability());
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
            Optional<RecipeHolder<InsolatingRecipe>> recipe = ModRecipeTypes.find(inventoryIn, level, ModRecipeTypes.INSOLATING);
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

    private void process() {
        RecipeWrapper inventoryIn = new RecipeWrapper(inputInv);

        if (lastRecipe == null || !lastRecipe.matches(inventoryIn, level) || !lastRecipe.getRequiredFluid().test(getCurrentFluidInTank())) {
            Optional<RecipeHolder<InsolatingRecipe>> recipe = ModRecipeTypes.find(inventoryIn, level, ModRecipeTypes.INSOLATING);
            if (!recipe.isPresent())
                return;
            lastRecipe = recipe.get().value();
        }

        ItemStack stackInSlot = inputInv.getStackInSlot(0);
        FluidStack fluidInSlot = getCurrentFluidInTank();
        if (lastRecipe.getIngredients().get(0).test(stackInSlot) && lastRecipe.getFluidIngredients().get(0).test(fluidInSlot) && lastRecipe.getRequiredFluid().amount() <= getCurrentFluidInTank().getAmount()) {
            if (lastRecipe.getParams().consumeInput())
                stackInSlot.shrink(1);
            fluidInSlot.shrink(lastRecipe.getRequiredFluid().amount());
            inputInv.setStackInSlot(0, stackInSlot);
            lastRecipe.rollResults(level.random).forEach(stack -> ItemHandlerHelper.insertItemStacked(outputInv, stack, false));
            sendData();
            setChanged();
        }
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

    private boolean canProcess(ItemStack stack) {
        ItemStackHandler tester = new ItemStackHandler(1);
        tester.setStackInSlot(0, stack);
        RecipeWrapper inventoryIn = new RecipeWrapper(tester);

        if (lastRecipe != null && lastRecipe.matches(inventoryIn, level) && lastRecipe.getRequiredFluid().test(getCurrentFluidInTank()) && lastRecipe.getRequiredFluid().amount() <= getCurrentFluidInTank().getAmount())
            return true;

        return ModRecipeTypes.find(inventoryIn, level, ModRecipeTypes.INSOLATING).isPresent();
    }

}
