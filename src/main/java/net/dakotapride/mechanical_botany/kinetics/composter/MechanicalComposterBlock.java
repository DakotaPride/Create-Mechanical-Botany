package net.dakotapride.mechanical_botany.kinetics.composter;

import com.simibubi.create.content.kinetics.base.KineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.data.Iterate;
import net.dakotapride.mechanical_botany.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class MechanicalComposterBlock extends KineticBlock implements IBE<MechanicalComposterBlockEntity>, ICogWheel {

    public MechanicalComposterBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.DOWN || face == Direction.UP;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn,
                                 BlockHitResult hit) {
        if (!player.getItemInHand(handIn)
                .isEmpty())
            return InteractionResult.PASS;
        if (worldIn.isClientSide)
            return InteractionResult.SUCCESS;

        withBlockEntityDo(worldIn, pos, composter -> {
            boolean emptyOutput = true;
            IItemHandlerModifiable inv = composter.outputInv;
            for (int slot = 0; slot < inv.getSlots(); slot++) {
                ItemStack stackInSlot = inv.getStackInSlot(slot);
                if (!stackInSlot.isEmpty())
                    emptyOutput = false;
                player.getInventory()
                        .placeItemBackInInventory(stackInSlot);
                inv.setStackInSlot(slot, ItemStack.EMPTY);
            }

            if (emptyOutput) {
                inv = composter.inputInv;
                for (int slot = 0; slot < inv.getSlots(); slot++) {
                    player.getInventory()
                            .placeItemBackInInventory(inv.getStackInSlot(slot));
                    inv.setStackInSlot(slot, ItemStack.EMPTY);
                }
            }

            composter.setChanged();
            composter.sendData();
        });

        return InteractionResult.SUCCESS;
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn) {
        super.updateEntityAfterFallOn(worldIn, entityIn);

        if (entityIn.level().isClientSide)
            return;
        if (!(entityIn instanceof ItemEntity itemEntity))
            return;
        if (!entityIn.isAlive())
            return;

        MechanicalComposterBlockEntity composter = null;
        for (BlockPos pos : Iterate.hereAndBelow(entityIn.blockPosition()))
            if (composter == null)
                composter = getBlockEntity(worldIn, pos);

        if (composter == null)
            return;

        LazyOptional<IItemHandler> capability = composter.getCapability(ForgeCapabilities.ITEM_HANDLER);
        if (!capability.isPresent())
            return;

        ItemStack remainder = capability.orElse(new ItemStackHandler())
                .insertItem(0, itemEntity.getItem(), false);
        if (remainder.isEmpty())
            itemEntity.discard();
        if (remainder.getCount() < itemEntity.getItem()
                .getCount())
            itemEntity.setItem(remainder);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public Class<MechanicalComposterBlockEntity> getBlockEntityClass() {
        return MechanicalComposterBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends MechanicalComposterBlockEntity> getBlockEntityType() {
        return ModBlockEntityTypes.COMPOSTER.get();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

}