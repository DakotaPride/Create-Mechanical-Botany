package net.dakotapride.mechanical_botany.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import net.dakotapride.mechanical_botany.ModBlocks;
import net.dakotapride.mechanical_botany.ModPartialModels;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import static net.createmod.catnip.lang.LangBuilder.resolveBuilders;

public class AnimatedMechanicalInsolator extends AnimatedKinetics {

    @Override
    public void draw(GuiGraphics graphics, int xOffset, int yOffset) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(xOffset, yOffset, 0);
        AllGuiTextures.JEI_SHADOW.render(graphics, -2, 33);
        matrixStack.translate(12, 38, 0);
        int scale = 22;

        blockElement(ModPartialModels.MECHANICAL_PUMP_COG_ROT)
                .rotateBlock(22.5, getCurrentAngle() * 2, 0)
                //.rotate(0, 90, 0)
                .scale(scale)
                .render(graphics);

        blockElement(ModBlocks.MECHANICAL_INSOLATOR.getDefaultState())
                .rotateBlock(22.5, 22.5, 0)
                .scale(scale)
                .render(graphics);

        matrixStack.popPose();
    }

}
