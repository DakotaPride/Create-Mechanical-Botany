package net.dakotapride.mechanical_botany.kinetics.composter;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.PonderStoryBoard;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.dakotapride.mechanical_botany.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

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
}