package net.dakotapride.mechanical_botany.kinetics.composter;

import com.simibubi.create.AllFluids;
import com.simibubi.create.content.fluids.drain.ItemDrainBlockEntity;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.*;
import net.dakotapride.mechanical_botany.ModFluids;
import net.dakotapride.mechanical_botany.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class ComposterPonderScenes {
    public static class Intro implements PonderStoryBoard {

        public Intro() {}

        @Override
        public void program(SceneBuilder builder, SceneBuildingUtil util) {
            CreateSceneBuilder scene = new CreateSceneBuilder(builder);
            scene.title("composter", "Breaking Down Plants Into Compost");
            scene.configureBasePlate(0, 0, 5);

            Selection belt = util.select().fromTo(1, 1, 5, 0, 1, 2)
                    .add(util.select().position(1, 2, 2));
            Selection beltCog = util.select().position(2, 0, 5);

            scene.world().showSection(util.select().layer(0)
                    .substract(beltCog), Direction.UP);

            BlockPos composter = util.grid().at(2, 2, 2);
            Selection composterSelect = util.select().position(2, 2, 2);
            Selection cogs = util.select().fromTo(3, 1, 2, 3, 2, 2);
            scene.world().setKineticSpeed(composterSelect, 0);

            scene.idle(5);
            scene.world().showSection(util.select().position(4, 1, 3), Direction.DOWN);
            scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
            scene.idle(10);
            scene.world().showSection(util.select().position(composter), Direction.DOWN);
            scene.idle(10);
            Vec3 composterTop = util.vector().topOf(composter);
            scene.overlay().showText(80)
                    .attachKeyFrame()
                    .text("Mechanical Composter will turn compostables into Compost")
                    .pointAt(composterTop)
                    .placeNearTarget();
            scene.idle(90);

            scene.world().showSection(cogs, Direction.DOWN);
            scene.idle(10);
            scene.world().setKineticSpeed(composterSelect, 32);
            scene.effects().indicateSuccess(composter);
            scene.idle(10);

            scene.overlay().showText(60)
                    .attachKeyFrame()
                    .colored(PonderPalette.GREEN)
                    .text("They can be powered from the side using cogwheels")
                    .pointAt(util.vector().topOf(composter.east()))
                    .placeNearTarget();
            scene.idle(70);

            ItemStack itemStack = new ItemStack(Items.ROSE_BUSH);
            Vec3 entitySpawn = util.vector().topOf(composter.above(3));

            ElementLink<EntityElement> entity1 =
                    scene.world().createItemEntity(entitySpawn, util.vector().of(0, 0.2, 0), itemStack);
            scene.idle(18);
            scene.world().modifyEntity(entity1, Entity::discard);
            scene.world().modifyBlockEntity(composter, MechanicalComposterBlockEntity.class,
                    ms -> ms.inputInv.setStackInSlot(0, itemStack));
            scene.idle(10);
            scene.overlay().showControls(composterTop, Pointing.DOWN, 25).withItem(itemStack);
            scene.idle(7);

            scene.overlay().showText(40)
                    .attachKeyFrame()
                    .text("Throw or Insert compostables at the top")
                    .pointAt(composterTop)
                    .placeNearTarget();
            scene.idle(60);

            scene.world().modifyBlockEntity(composter, MechanicalComposterBlockEntity.class,
                    ms -> ms.inputInv.setStackInSlot(0, ItemStack.EMPTY));

            scene.overlay().showText(70)
                    .text("After some time, compost can be retrieved via Right-click")
                    .pointAt(util.vector().blockSurface(composter, Direction.WEST))
                    .placeNearTarget();
            scene.idle(80);

            ItemStack itemStack2 = ModItems.COMPOST.asStack();
            scene.overlay().showControls(util.vector().blockSurface(composter, Direction.NORTH), Pointing.RIGHT, 40).rightClick()
                    .withItem(itemStack2);
            scene.idle(50);

            scene.addKeyframe();
            scene.world().showSection(beltCog, Direction.UP);
            scene.world().showSection(belt, Direction.EAST);
            scene.idle(15);

            BlockPos beltPos = util.grid().at(1, 1, 2);
            scene.world().createItemOnBelt(beltPos, Direction.EAST, itemStack2);
            scene.idle(15);
            scene.world().createItemOnBelt(beltPos, Direction.EAST, new ItemStack(ModItems.COMPOST.get()));
            scene.idle(20);

            scene.overlay().showText(50)
                    .text("The outputs can also be extracted by automation through any side")
                    .pointAt(util.vector().blockSurface(composter, Direction.WEST)
                            .add(-.5, .4, 0))
                    .placeNearTarget();
            scene.idle(60);
        }
    }

    public static class LayeredComposters implements PonderStoryBoard {

        public LayeredComposters() {}

        @Override
        public void program(SceneBuilder builder, SceneBuildingUtil util) {
            CreateSceneBuilder scene = new CreateSceneBuilder(builder);
            scene.title("layered_composters", "Transporting Rotational Power With Layered Composters");
            scene.configureBasePlate(0, 0, 5);

            Selection belt = util.select().fromTo(1, 1, 5, 0, 1, 2)
                    .add(util.select().position(1, 2, 2));
            Selection beltCog = util.select().position(2, 0, 5);

            scene.world().showSection(util.select().layer(0)
                    .substract(beltCog), Direction.UP);

            BlockPos composter = util.grid().at(2, 2, 2);
            Selection composterSelect = util.select().position(2, 2, 2);
            BlockPos composter2 = util.grid().at(2, 4, 2);
            Selection composterSelect2 = util.select().position(2, 4, 2);
            Selection cogs = util.select().fromTo(5, 1, 2, 3, 1, 2);
            scene.world().setKineticSpeed(composterSelect, 0);
            scene.idle(5);

            scene.world().showSection(cogs, Direction.DOWN);
            scene.idle(10);
            scene.world().setKineticSpeed(composterSelect, -32);
            scene.idle(5);

            scene.world().showSection(util.select().position(4, 1, 3), Direction.DOWN);
            scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
            scene.idle(10);
            scene.world().showSection(util.select().position(composter), Direction.DOWN);
            scene.idle(10);
            scene.effects().indicateSuccess(composter);
            Vec3 composterTop = util.vector().topOf(composter);
            scene.overlay().showText(80)
                    .attachKeyFrame()
                    .text("Mechanical Composters can also be powered from the bottom")
                    .pointAt(composterTop)
                    .placeNearTarget();
            scene.idle(90);

            scene.overlay().showText(80)
                    .attachKeyFrame()
                    .text("Rotational Power flows through the Mechanical Composter's cog")
                    .pointAt(composterTop)
                    .placeNearTarget();
            scene.idle(90);
            //scene.world().setKineticSpeed(composterSelect2, 32);
            scene.world().showSection(util.select().position(composter2), Direction.DOWN);
            scene.world().showSection(util.select().position(composter2.below()), Direction.DOWN);
            scene.world().showSection(util.select().position(composter2.west(1)), Direction.DOWN);
            //scene.effects().indicateSuccess(composter2);
            scene.idle(10);
            Vec3 composterTop2 = util.vector().topOf(composter2);
            scene.overlay().showText(80)
                    .attachKeyFrame()
                    .text("This offers for some unique automation potential")
                    .pointAt(composterTop2)
                    .placeNearTarget();
            scene.idle(90);

            ItemStack itemStack = new ItemStack(ModItems.COMPOST.get());
            Vec3 entitySpawn = util.vector().of(1,  4, 2.2);

            ItemStack itemStack2 = ModItems.COMPOST.asStack();

            scene.addKeyframe();
            scene.world().showSection(beltCog, Direction.UP);
            scene.world().showSection(belt, Direction.EAST);
            scene.idle(15);

            BlockPos beltPos = util.grid().at(1, 1, 2);
            BlockPos beltPos2 = util.grid().at(0, 1, 2);

            ElementLink<EntityElement> entity1 =
                    scene.world().createItemEntity(entitySpawn, util.vector().of(0,  -0.1, 0), itemStack);
            scene.world().flapFunnel(util.grid().at(1, 4, 2), true);
            scene.world().createItemOnBelt(beltPos, Direction.EAST, itemStack2);
            scene.idle(9);
            scene.world().modifyEntity(entity1, Entity::discard);
            scene.idle(2);
            scene.world().createItemOnBelt(beltPos2, Direction.DOWN, itemStack);

            //scene.idle(4);
            scene.idle(20);
        }
    }

    public static class CreatingLiquidCompost implements PonderStoryBoard {

        public CreatingLiquidCompost() {}

        @Override
        public void program(SceneBuilder builder, SceneBuildingUtil util) {
            CreateSceneBuilder scene = new CreateSceneBuilder(builder);
            scene.title("compost_creation", "Processing Compost Into Usable Fluids");
            scene.configureBasePlate(0, 0, 5);

            // Powers belt
            Selection cogs0 = util.select().fromTo(0, 0, 1, 0, 1, 0);
            // Powers pump2
            Selection cogs1 = util.select().fromTo(6, 0, 1, 6, 1, 0);
            // Powers pump1
            Selection cogs2 = util.select().fromTo(3, 0, 5, 4, 1, 5);
            Selection selection0 = util.select().fromTo(4, 1, 4, 4, 4, 3);
            Selection selection1 = util.select().position(5, 1, 0);
            Selection selection2 = util.select().fromTo(4, 4, 1, 4, 4, 2);
            // Powers pump0
            Selection cogs3 = util.select().fromTo(0, 0, 4, 0, 1, 2);
            Selection pump0 = util.select().fromTo(1, 1, 3, 2, 1, 4);
            Selection pump1 = util.select().fromTo(3, 2, 2, 4, 1, 2);
            Selection pump2 = util.select().fromTo(4, 2, 1, 4, 1, 0);
            Selection tank0 = util.select().fromTo(3, 1, 3, 3, 4, 3);
            Selection tank1 = util.select().fromTo(5, 1, 1, 5, 4, 1);
            BlockPos tank0Position = util.grid().at(3, 1, 3);
            Selection belt = util.select().fromTo(1, 1, 0, 1, 1, 2);
            BlockPos drain = util.grid().at(1, 1, 2);
            Selection drainSelect = util.select().position(drain);
            Selection basinAndCo = util.select().fromTo(3, 1, 1, 3, 4, 1);
            BlockPos basinPosition = util.grid().at(3, 2, 1);
            BlockPos burnerPosition = util.grid().at(3, 1, 1);

            scene.idle(5);
            scene.world().showSection(util.select().layer(0).substract(cogs1).substract(cogs2).substract(cogs3), Direction.UP);
            scene.world().showSection(cogs0, Direction.DOWN);
            scene.idle(7);
            scene.world().showSection(belt, Direction.DOWN);

            ItemStack itemStack0 = new ItemStack(ModItems.COMPOST.get());
            ItemStack itemStack1 = new ItemStack(ModItems.COMPOST.get());
            ItemStack itemStack2 = new ItemStack(ModItems.COMPOST.get());
            ItemStack itemStack3 = new ItemStack(ModItems.COMPOST.get());

            scene.overlay().showText(80)
                    .text("Putting Compost into an Item Drain will provide small amounts of Liquid Compost")
                    .attachKeyFrame()
                    .placeNearTarget()
                    .pointAt(util.vector().blockSurface(drain.west(), Direction.UP));
            scene.idle(20);

            scene.world().createItemOnBelt(util.grid().at(1, 1, 0), Direction.NORTH, itemStack0);
            scene.idle(24);

            scene.world().createItemOnBelt(util.grid().at(1, 1, 0), Direction.NORTH, itemStack1);
            scene.idle(24);

            scene.world().createItemOnBelt(util.grid().at(1, 1, 0), Direction.NORTH, itemStack2);
            scene.idle(24);

            scene.world().createItemOnBelt(util.grid().at(1, 1, 0), Direction.NORTH, itemStack3);
            scene.idle(50);

            scene.addKeyframe();
            scene.idle(7);
            scene.world().showSection(cogs3, Direction.DOWN);
            scene.world().showSection(pump0, Direction.DOWN);
            scene.world().showSection(tank0, Direction.DOWN);
            scene.idle(24);

            FluidStack content0 = new FluidStack(ModFluids.COMPOST.get().getSource(), 500);

            for (int i = 0; i < 4; i++) {
                scene.world().modifyBlockEntity(drain, ItemDrainBlockEntity.class,
                        be -> {
                            IFluidHandler fh = be.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos(), null);
                            if (fh != null)
                                fh.drain(50, IFluidHandler.FluidAction.EXECUTE);
                        });

                scene.world().modifyBlockEntity(tank0Position, FluidTankBlockEntity.class, be -> be.getTankInventory()
                        .fill(content0, IFluidHandler.FluidAction.EXECUTE));

                scene.idle(7);
            }

            scene.addKeyframe();
            scene.idle(14);
            scene.world().showSection(tank1, Direction.DOWN);
            scene.world().showSection(basinAndCo, Direction.DOWN);
            scene.idle(7);
            scene.world().showSection(cogs1, Direction.DOWN);
            scene.world().showSection(pump2, Direction.DOWN);
            scene.world().showSection(selection1, Direction.DOWN);
            scene.world().showSection(selection2, Direction.DOWN);
            //scene.idle(7);

            scene.world().showSection(cogs2, Direction.DOWN);
            scene.world().showSection(selection0, Direction.DOWN);
            scene.world().showSection(pump1, Direction.DOWN);
            scene.idle(14);
            scene.world().modifyBlockEntity(util.grid().at(3, 2, 1), BasinBlockEntity.class,
                    be -> {
                        IFluidHandler handler = be.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos(), null);
                        if (handler != null)
                            handler.fill(content0, IFluidHandler.FluidAction.EXECUTE);
                    });
            scene.idle(7);
            scene.overlay().showText(80)
                    .text("Mixing Liquid Compost and Lava together provides Molten Liquid Compost")
                    .attachKeyFrame()
                    .placeNearTarget()
                    .pointAt(util.vector().blockSurface(basinPosition.west(), Direction.UP));
            scene.idle(90);
            scene.overlay().showControls(util.vector().blockSurface(burnerPosition, Direction.WEST), Pointing.LEFT, 15).rightClick().withItem(new ItemStack(Items.OAK_PLANKS));
            scene.idle(3);
            scene.world().modifyBlock(burnerPosition, s -> s.setValue(BlazeBurnerBlock.HEAT_LEVEL, BlazeBurnerBlock.HeatLevel.KINDLED), false);
            scene.idle(7);
            Class<MechanicalMixerBlockEntity> type = MechanicalMixerBlockEntity.class;
            scene.world().modifyBlockEntity(util.grid().at(3, 4, 1), type, MechanicalMixerBlockEntity::startProcessingBasin);
            scene.idle(40);

            FluidStack content1 = new FluidStack(ModFluids.MOLTEN_COMPOST.get().getSource(), 1000);

            scene.addKeyframe();
            scene.idle(4);
            for (int i = 0; i < 5; i++) {
                scene.idle(7);
                scene.world().modifyBlockEntity(util.grid().at(3, 2, 1), BasinBlockEntity.class,
                        be -> {
                            IFluidHandler handler = be.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos(), null);
                            if (handler != null)
                                handler.drain(new FluidStack(content0.getFluid(), content0.getAmount() / 5), IFluidHandler.FluidAction.EXECUTE);
                        });
                scene.world().modifyBlockEntity(util.grid().at(3, 2, 1), BasinBlockEntity.class,
                        be -> {
                            IFluidHandler handler = be.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos(), null);
                            if (handler != null)
                                handler.drain(new FluidStack(Fluids.LAVA, 1000 / 5), IFluidHandler.FluidAction.EXECUTE);
                        });
            }
            scene.world().modifyBlockEntity(util.grid().at(3, 2, 1), BasinBlockEntity.class,
                    be -> {
                        IFluidHandler handler = be.getLevel().getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos(), null);
                        if (handler != null)
                            handler.fill(content1, IFluidHandler.FluidAction.EXECUTE);
                    });
            scene.idle(14);
            scene.overlay().showControls(util.vector().blockSurface(basinPosition, Direction.WEST), Pointing.LEFT, 25).withItem(new ItemStack(ModFluids.MOLTEN_COMPOST.get().getBucket()));
            scene.idle(20);

        }
    }
}