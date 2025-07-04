package net.dakotapride.mechanical_botany.kinetics.composter;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.sound.SoundScapes;
import com.simibubi.create.foundation.utility.CreateLang;
import net.createmod.catnip.math.VecHelper;
import net.dakotapride.mechanical_botany.CreateMechanicalBotany;
import net.dakotapride.mechanical_botany.ModBlockEntityTypes;
import net.dakotapride.mechanical_botany.ModRecipeTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class MechanicalComposterBlockEntity extends KineticBlockEntity implements IHaveGoggleInformation {

    public SmartInventory inputInv;
    public SmartInventory outputInv;
    public IItemHandler itemCapability;
    public int timer;
    private CompostingRecipe lastRecipe;

    public MechanicalComposterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        inputInv = new MechanicalComposterInventory(1, this);
        outputInv = new MechanicalComposterInventory(9, this);
        itemCapability = new MechanicalComposterInvWrapper(inputInv, outputInv);
    }

    private class MechanicalComposterInvWrapper extends CombinedInvWrapper {
        public MechanicalComposterInvWrapper(SmartInventory input, SmartInventory output) {
            super(input, output);
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

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
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
        }

        //addBlankSpace(tooltip);

        return super.addToGoggleTooltip(tooltip, isPlayerSneaking);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntityTypes.COMPOSTER.get(),
                (be, context) -> be.itemCapability
        );
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this));
        super.addBehaviours(behaviours);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void tickAudio() {
        super.tickAudio();

        if (getSpeed() == 0)
            return;
        if (inputInv.getStackInSlot(0)
                .isEmpty())
            return;

        float pitch = Mth.clamp((Math.abs(getSpeed()) / 256f) + .45f, .85f, 1f);
        SoundScapes.play(SoundScapes.AmbienceGroup.CRUSHING, worldPosition, pitch);
    }

    @Override
    public void tick() {
        super.tick();

        if (getSpeed() == 0)
            return;
        for (int i = 0; i < outputInv.getSlots(); i++)
            if (outputInv.getStackInSlot(i)
                    .getCount() == outputInv.getSlotLimit(i))
                return;

        if (timer > 0) {
            timer -= getProcessingSpeed();

            if (level.isClientSide) {
                spawnParticles();
                return;
            }
            if (timer <= 0)
                process();
            return;
        }

        if (inputInv.getStackInSlot(0)
                .isEmpty())
            return;

        RecipeWrapper inventoryIn = new RecipeWrapper(inputInv);
        if (lastRecipe == null || !lastRecipe.matches(inventoryIn, level)) {
            Optional<RecipeHolder<CompostingRecipe>> recipe = ModRecipeTypes.find(inventoryIn, level, ModRecipeTypes.COMPOSTING);
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
        invalidateCapabilities();
    }

    @Override
    public void destroy() {
        super.destroy();
        ItemHelper.dropContents(level, worldPosition, inputInv);
        ItemHelper.dropContents(level, worldPosition, outputInv);
    }

    private void process() {
        RecipeWrapper inventoryIn = new RecipeWrapper(inputInv);

        if (lastRecipe == null || !lastRecipe.matches(inventoryIn, level)) {
            Optional<RecipeHolder<CompostingRecipe>> recipe = ModRecipeTypes.find(inventoryIn, level, ModRecipeTypes.COMPOSTING);
            if (!recipe.isPresent())
                return;
            lastRecipe = recipe.get().value();
        }

        ItemStack stackInSlot = inputInv.getStackInSlot(0);
        stackInSlot.shrink(1);
        inputInv.setStackInSlot(0, stackInSlot);
        lastRecipe.rollResults()
                .forEach(stack -> ItemHandlerHelper.insertItemStacked(outputInv, stack, false));

        sendData();
        setChanged();
    }

    public void spawnParticles() {
        ItemStack stackInSlot = inputInv.getStackInSlot(0);
        if (stackInSlot.isEmpty())
            return;

        ItemParticleOption data = new ItemParticleOption(ParticleTypes.ITEM, stackInSlot);
        float angle = level.random.nextFloat() * 360;
        Vec3 offset = new Vec3(0, 0, 0.5f);
        offset = VecHelper.rotate(offset, angle, Direction.Axis.Y);
        Vec3 target = VecHelper.rotate(offset, getSpeed() > 0 ? 25 : -25, Direction.Axis.Y);

        Vec3 center = offset.add(VecHelper.getCenterOf(worldPosition));
        target = VecHelper.offsetRandomly(target.subtract(offset), level.random, 1 / 128f);
        level.addParticle(data, center.x, center.y, center.z, target.x, target.y, target.z);
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

        if (lastRecipe != null && lastRecipe.matches(inventoryIn, level))
            return true;

        return ModRecipeTypes.find(inventoryIn, level, ModRecipeTypes.COMPOSTING).isPresent();
    }

}
