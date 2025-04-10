package com.cursee.overclocked_watches.client.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

public abstract class RendererUtil {

    public static ModelPart bakeLayer(ModelLayerLocation layerLocation) {
        return Minecraft.getInstance().getEntityModels().bakeLayer(layerLocation);
    }

    public static HumanoidArm getArmSide(LivingEntity entity, InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? entity.getMainArm() : entity.getMainArm().getOpposite();
    }

    public static InteractionHand getInteractionHand(LivingEntity entity, HumanoidArm armSide) {
        return armSide == entity.getMainArm() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
    }

    public static MeshDefinition createEmptyArms(CubeListBuilder leftArm, CubeListBuilder rightArm, boolean hasSlimArms) {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0);

        mesh.getRoot().addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
        mesh.getRoot().addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);

        float armWidth = hasSlimArms ? 3 : 4;
        mesh.getRoot().getChild("left_arm").addOrReplaceChild(
                "artifact",
                leftArm,
                PartPose.offset(- 1 + armWidth / 2, 10, 0)
        );
        mesh.getRoot().getChild("right_arm").addOrReplaceChild(
                "artifact",
                rightArm,
                PartPose.offset(1 - armWidth / 2, 10, 0)
        );

        return mesh;
    }

    static float calc(float value) {
        return -1 - value / 2;
    }

    public static MeshDefinition createWatchModel(boolean hasSlimArms) {
        // Create two CubeListBuilders to define the shapes of the left and right arms of the model
        CubeListBuilder leftArm = CubeListBuilder.create();
        CubeListBuilder rightArm = CubeListBuilder.create();

        // Determine arm dimensions based on whether the model uses slim arms
        float armWidth = hasSlimArms ? 3 : 4; // Slim arms are narrower (3 units), regular arms are wider (4 units)
        float armDepth = 4; // Depth of the arm (front-to-back thickness)
        float height = -4; // Height (y-coordinate) where the arms are positioned on the model

        // Add the first rectangular piece to the left and right arms (top or bottom strap of the watch)
        leftArm.texOffs(0, 0); // Set texture offset for this part of the left arm
        leftArm.addBox(calc(armWidth), height, calc(armDepth), armWidth + 2, 2, 1); // Add a box to represent part of the strap
        rightArm.texOffs(16, 0); // Set texture offset for the right arm
        rightArm.addBox(calc(armWidth), height, calc(armDepth), armWidth + 2, 2, 1); // Add a corresponding box for the right arm

        // Add the second rectangular piece to the left and right arms (opposite strap)
        leftArm.texOffs(0, 3); // Update texture offset for this part
        leftArm.addBox(calc(armWidth), height, armDepth / 2, armWidth + 2, 2, 1); // Add a box for the opposite side of the strap
        rightArm.texOffs(16, 3);
        rightArm.addBox(calc(armWidth), height, armDepth / 2, armWidth + 2, 2, 1);

        // Add the first vertical piece (side of the watch strap) for the left and right arms
        leftArm.texOffs(0, 6);
        leftArm.addBox(armWidth / 2, height, -armDepth / 2, 1, 2, armDepth); // A vertical rectangle on one side of the strap
        rightArm.texOffs(16, 6);
        rightArm.addBox(armWidth / 2, height, -armDepth / 2, 1, 2, armDepth);

        // Add the second vertical piece (opposite side of the strap) for the left and right arms
        leftArm.texOffs(0, 12);
        leftArm.addBox(calc(armWidth), height, -armDepth / 2, 1, 2, armDepth); // A vertical rectangle on the other side
        rightArm.texOffs(16, 12);
        rightArm.addBox(calc(armWidth), height, -armDepth / 2, 1, 2, armDepth);

        // Add the watch face
        leftArm.texOffs(0, 18);
        leftArm.addBox(
                calc(armWidth) + 6, -5, -2,
                -1, 4, 4);
        rightArm.texOffs(16, 18);
        rightArm.addBox(
                calc(armWidth) - 1, -5, -2,
                1, 4, 4);

        // Return a MeshDefinition combining the left and right arms, with a utility method for empty arms
        return RendererUtil.createEmptyArms(leftArm, rightArm, hasSlimArms);
    }
}
