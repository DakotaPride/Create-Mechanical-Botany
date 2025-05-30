package net.dakotapride.mechanical_botany.kinetics.insolator;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.PonderStoryBoard;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class InsolatorPonderScenes {
    public static class Intro implements PonderStoryBoard {

        public Intro() {}

        @Override
        public void program(SceneBuilder builder, SceneBuildingUtil util) {
            CreateSceneBuilder scene = new CreateSceneBuilder(builder);
            scene.title("insolator", "Growing Plants in the Mechanical Insolator");
            scene.configureBasePlate(0, 0, 5);

            Selection belt = util.select().fromTo(1, 1, 5, 0, 1, 2)
                    .add(util.select().position(1, 2, 2));
            Selection beltCog = util.select().position(2, 0, 5);

            scene.world().showSection(util.select().layer(0)
                    .substract(beltCog), Direction.UP);

            BlockPos insolator = util.grid().at(2, 2, 2);
            Selection insolatorSelect = util.select().position(2, 2, 2);
            BlockPos pump = util.grid().at(2, 1, 2);
            Selection pumpSelect = util.select().position(2, 1, 2);
            Selection cogs = util.select().fromTo(3, 1, 2, 3, 2, 2);
            scene.world().setKineticSpeed(insolatorSelect, 0);
            scene.world().setKineticSpeed(pumpSelect, 0);

            scene.idle(5);
            scene.world().showSection(util.select().position(4, 1, 3), Direction.DOWN);
            scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
            scene.idle(10);
            scene.world().showSection(util.select().position(insolator), Direction.DOWN);
            scene.idle(10);
            Vec3 insolatorTop = util.vector().topOf(insolator);
            scene.overlay().showText(80)
                    .attachKeyFrame()
                    .text("Mechanical Insolators will reprocess certain plants provided to them")
                    .pointAt(insolatorTop)
                    .placeNearTarget();
            scene.idle(90);

            scene.world().showSection(cogs, Direction.DOWN);
            scene.idle(10);
            scene.world().setKineticSpeed(insolatorSelect, 32);
            scene.world().setKineticSpeed(pumpSelect, 32);
            scene.effects().indicateSuccess(insolator);
            scene.idle(10);

            scene.overlay().showText(60)
                    .attachKeyFrame()
                    .colored(PonderPalette.GREEN)
                    .text("They can be powered from the side using cogwheels")
                    .pointAt(util.vector().topOf(insolator.east()))
                    .placeNearTarget();
            scene.idle(70);

            scene.overlay().showText(100)
                    .attachKeyFrame()
                    .text("Mechanical Insolators also require a fluid to be provided to them in order to process")
                    .pointAt(util.vector().topOf(pump))
                    .placeNearTarget();
            scene.idle(110);
            scene.overlay().showText(100)
                    .attachKeyFrame()
                    .text("This fluid is dependent on the plant, but typically will be Water")
                    .pointAt(util.vector().topOf(pump))
                    .placeNearTarget();
            scene.idle(110);

            ItemStack itemStack = new ItemStack(Items.ALLIUM);
            Vec3 entitySpawn = util.vector().topOf(insolator.above(3));

            ElementLink<EntityElement> entity1 =
                    scene.world().createItemEntity(entitySpawn, util.vector().of(0, 0.2, 0), itemStack);
            scene.idle(18);
            scene.world().modifyEntity(entity1, Entity::discard);
            scene.world().modifyBlockEntity(insolator, MechanicalInsolatorBlockEntity.class,
                    ms -> ms.inputInv.setStackInSlot(0, itemStack));
            scene.idle(10);
            scene.overlay().showControls(insolatorTop, Pointing.DOWN, 25).withItem(itemStack);
            scene.idle(7);

            scene.overlay().showText(40)
                    .attachKeyFrame()
                    .text("Throw or Insert plants at the top")
                    .pointAt(insolatorTop)
                    .placeNearTarget();
            scene.idle(60);

            scene.world().modifyBlockEntity(insolator, MechanicalInsolatorBlockEntity.class,
                    ms -> ms.inputInv.setStackInSlot(0, ItemStack.EMPTY));

            scene.overlay().showText(70)
                    .text("After some time, the grown results can be retrieved via Right-click")
                    .pointAt(util.vector().blockSurface(insolator, Direction.WEST))
                    .placeNearTarget();
            scene.idle(80);

            ItemStack itemStack2 = Items.ALLIUM.getDefaultInstance();
            scene.overlay().showControls(util.vector().blockSurface(insolator, Direction.NORTH), Pointing.RIGHT, 40).rightClick()
                    .withItem(itemStack2);
            scene.idle(50);

            scene.addKeyframe();
            scene.world().showSection(beltCog, Direction.UP);
            scene.world().showSection(belt, Direction.EAST);
            scene.idle(15);

            BlockPos beltPos = util.grid().at(1, 1, 2);
            scene.world().createItemOnBelt(beltPos, Direction.EAST, itemStack2);
            scene.idle(15);
            scene.world().createItemOnBelt(beltPos, Direction.EAST, new ItemStack(Items.ALLIUM));
            scene.idle(20);

            scene.overlay().showText(50)
                    .text("The outputs can also be extracted by automation through any side")
                    .pointAt(util.vector().blockSurface(insolator, Direction.WEST)
                            .add(-.5, .4, 0))
                    .placeNearTarget();
            scene.idle(60);
        }
    }

    public static class MoltenCompostUsage implements PonderStoryBoard {

        public MoltenCompostUsage() {}

        @Override
        public void program(SceneBuilder builder, SceneBuildingUtil util) {
            CreateSceneBuilder scene = new CreateSceneBuilder(builder);
            scene.title("molten_compost_use", "Using Molten Compost Within a Mechanical Insolator");
            scene.configureBasePlate(0, 0, 5);

            Selection belt = util.select().fromTo(1, 2, 5, 0, 1, 2)
                    .add(util.select().position(1, 3, 2));
            Selection beltCog = util.select().position(2, 0, 5);

            scene.world().showSection(util.select().layer(0)
                    .substract(beltCog), Direction.UP);
            //scene.world().showSection(util.select().everywhere(), Direction.UP);

            BlockPos insolator = util.grid().at(2, 3, 2);
            Selection insolatorSelect = util.select().position(2, 3, 2);
            BlockPos pump = util.grid().at(2, 2, 2);
            Selection pumpSelect = util.select().position(2, 2, 2);
            Selection cogs = util.select().fromTo(2, 3, 3, 2, 1, 3);
            scene.world().setKineticSpeed(insolatorSelect, 0);
            scene.world().setKineticSpeed(pumpSelect, 0);

            scene.idle(5);
            scene.world().showSection(util.select().fromTo(5, 1, 3, 3, 1, 3), Direction.DOWN);
            scene.world().showSection(util.select().fromTo(3, 1, 2, 3, 4, 2), Direction.DOWN);
            scene.idle(10);
            scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
            scene.world().showSection(util.select().position(pump), Direction.DOWN);
            scene.world().showSection(util.select().position(insolator), Direction.DOWN);
            scene.idle(10);
            Vec3 insolatorTop = util.vector().topOf(insolator);
            scene.overlay().showText(80)
                    .attachKeyFrame()
                    .text("Molten Liquid Compost can be used to produce Wither Roses")
                    .pointAt(insolatorTop)
                    .placeNearTarget();
            scene.idle(90);

            scene.world().showSection(cogs, Direction.DOWN);
            scene.idle(10);
            scene.world().setKineticSpeed(insolatorSelect, 32);
            scene.world().setKineticSpeed(pumpSelect, 32);
            scene.effects().indicateSuccess(insolator);
            scene.idle(10);

            ItemStack itemStack = new ItemStack(Items.WITHER_ROSE);
            Vec3 entitySpawn = util.vector().topOf(insolator.above(3));

            ElementLink<EntityElement> entity1 =
                    scene.world().createItemEntity(entitySpawn, util.vector().of(0, 0.2, 0), itemStack);
            scene.idle(18);
            scene.world().modifyEntity(entity1, Entity::discard);
            scene.world().modifyBlockEntity(insolator, MechanicalInsolatorBlockEntity.class,
                    ms -> ms.inputInv.setStackInSlot(0, itemStack));
            scene.idle(40);

            scene.world().modifyBlockEntity(insolator, MechanicalInsolatorBlockEntity.class,
                    ms -> ms.inputInv.setStackInSlot(0, ItemStack.EMPTY));

            ItemStack itemStack2 = Items.WITHER_ROSE.getDefaultInstance();
            scene.overlay().showControls(util.vector().blockSurface(insolator, Direction.NORTH), Pointing.RIGHT, 40).rightClick()
                    .withItem(itemStack2);
            scene.idle(50);

            scene.addKeyframe();
            scene.world().showSection(beltCog, Direction.UP);
            scene.world().showSection(belt, Direction.EAST);
            scene.idle(15);

            BlockPos beltPos = util.grid().at(1, 2, 2);
            scene.world().createItemOnBelt(beltPos, Direction.EAST, itemStack2);
            scene.idle(15);
            scene.world().createItemOnBelt(beltPos, Direction.EAST, new ItemStack(Items.WITHER_ROSE));
            scene.idle(20);
        }
    }

    public static class CompostUsage implements PonderStoryBoard {

        public CompostUsage() {}

        @Override
        public void program(SceneBuilder builder, SceneBuildingUtil util) {
            CreateSceneBuilder scene = new CreateSceneBuilder(builder);
            scene.title("compost_use", "Using Liquid Compost Within a Mechanical Insolator");
            scene.configureBasePlate(0, 0, 5);

            Selection belt = util.select().fromTo(1, 2, 5, 0, 1, 2)
                    .add(util.select().position(1, 3, 2));
            Selection beltCog = util.select().position(2, 0, 5);

            scene.world().showSection(util.select().layer(0)
                    .substract(beltCog), Direction.UP);
            //scene.world().showSection(util.select().everywhere(), Direction.UP);

            BlockPos insolator = util.grid().at(2, 3, 2);
            Selection insolatorSelect = util.select().position(2, 3, 2);
            BlockPos pump = util.grid().at(2, 2, 2);
            Selection pumpSelect = util.select().position(2, 2, 2);
            Selection cogs = util.select().fromTo(2, 3, 3, 2, 1, 3);
            scene.world().setKineticSpeed(insolatorSelect, 0);
            scene.world().setKineticSpeed(pumpSelect, 0);

            scene.idle(5);
            scene.world().showSection(util.select().fromTo(5, 1, 3, 3, 1, 3), Direction.DOWN);
            scene.world().showSection(util.select().fromTo(3, 1, 2, 3, 4, 2), Direction.DOWN);
            scene.idle(10);
            scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
            scene.world().showSection(util.select().position(pump), Direction.DOWN);
            scene.world().showSection(util.select().position(insolator), Direction.DOWN);
            scene.idle(10);
            Vec3 insolatorTop = util.vector().topOf(insolator);
            scene.overlay().showText(80)
                    .attachKeyFrame()
                    .text("Plants gathered from Sniffers require Liquid Compost in order to be reprocessed")
                    .pointAt(insolatorTop)
                    .placeNearTarget();
            scene.idle(90);

            scene.world().showSection(cogs, Direction.DOWN);
            scene.idle(10);
            scene.world().setKineticSpeed(insolatorSelect, 32);
            scene.world().setKineticSpeed(pumpSelect, 32);
            scene.effects().indicateSuccess(insolator);
            scene.idle(10);

            ItemStack itemStack = new ItemStack(Items.TORCHFLOWER_SEEDS);
            Vec3 entitySpawn = util.vector().topOf(insolator.above(3));

            ElementLink<EntityElement> entity1 =
                    scene.world().createItemEntity(entitySpawn, util.vector().of(0, 0.2, 0), itemStack);
            scene.idle(18);
            scene.world().modifyEntity(entity1, Entity::discard);
            scene.world().modifyBlockEntity(insolator, MechanicalInsolatorBlockEntity.class,
                    ms -> ms.inputInv.setStackInSlot(0, itemStack));
            scene.idle(40);

            scene.world().modifyBlockEntity(insolator, MechanicalInsolatorBlockEntity.class,
                    ms -> ms.inputInv.setStackInSlot(0, ItemStack.EMPTY));

            ItemStack itemStack2 = Items.TORCHFLOWER.getDefaultInstance();
            scene.overlay().showControls(util.vector().blockSurface(insolator, Direction.NORTH), Pointing.RIGHT, 40).rightClick()
                    .withItem(itemStack2);
            scene.idle(50);
            scene.overlay().showControls(util.vector().blockSurface(insolator, Direction.NORTH), Pointing.RIGHT, 40).rightClick()
                    .withItem(itemStack);
            scene.idle(50);

            scene.addKeyframe();
            scene.world().showSection(beltCog, Direction.UP);
            scene.world().showSection(belt, Direction.EAST);
            scene.idle(15);

            BlockPos beltPos = util.grid().at(1, 2, 2);
            scene.world().createItemOnBelt(beltPos, Direction.EAST, itemStack2);
            scene.idle(15);
            scene.world().createItemOnBelt(beltPos, Direction.EAST, new ItemStack(Items.TORCHFLOWER_SEEDS));
            scene.idle(20);
        }
    }
}