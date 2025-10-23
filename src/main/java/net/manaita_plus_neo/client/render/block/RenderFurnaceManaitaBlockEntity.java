package net.manaita_plus_neo.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.manaita_plus_neo.block.ManaitaFurnaceBlock;
import net.manaita_plus_neo.block.data.Data;
import net.manaita_plus_neo.block.entity.ManaitaFurnaceBlockEntity;
import net.manaita_plus_neo.item.ModItems;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;

@OnlyIn(Dist.CLIENT)
public class RenderFurnaceManaitaBlockEntity implements BlockEntityRenderer<ManaitaFurnaceBlockEntity> {
    private final EntityRenderDispatcher entityRenderer;
    private final ItemStack stack;

    public RenderFurnaceManaitaBlockEntity(BlockEntityRendererProvider.Context context) {
        this.entityRenderer = context.getEntityRenderer();
        stack = new ItemStack(ModItems.FURNACE_BLOCK.get());
        CompoundTag tag = new CompoundTag();
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    @Override
    public void render(ManaitaFurnaceBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState blockState = blockEntity.getBlockState();

        Direction direction = blockState.getValue(Data.FACING);
        Direction wall = blockState.getValue(Data.WALL);

        poseStack.pushPose();

        switch (wall) {
            case NORTH:
                poseStack.translate(0.5F, 0.5F, 0.0F);
                poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(-90.0F));
                break;
            case SOUTH:
                poseStack.translate(0.5F, 0.5F, 1F);
                poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(90.0F));
                break;
            case WEST:
                poseStack.translate(0.0F, 0.5F, 0.5F);
                poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
                poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(90.0F));
                break;
            case EAST:
                poseStack.translate(1F, 0.5F, 0.5F);
                poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
                poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(90.0F));
                break;
            case UP:
                poseStack.translate(0.5F, 1F, 0.5F);
                switch (direction) {
                    case NORTH:
                        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
                        break;
                    case SOUTH:
                        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-90.0F));
                        break;
                    case WEST:
                        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
                        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(90.0F));
                        break;
                    case EAST:
                        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
                        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(-90.0F));
                        break;
                }
                break;
            case DOWN:
                poseStack.translate(0.5F, 0.0F, 0.5F);
                switch (direction) {
                    case NORTH:
                        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
                        break;
                    case SOUTH:
                        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-90.0F));
                        break;
                    case WEST:
                        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
                        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(90.0F));
                        break;
                    case EAST:
                        poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90.0F));
                        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(-90.0F));
                        break;
                }
                break;
        }

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        Block block = blockEntity.getBlockState().getBlock();
        if (block instanceof ManaitaFurnaceBlock) {
            var customData = stack.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                var tag = customData.getUnsafe();
                tag.putInt("ManaitaType", blockEntity.getBlockState().getValue(Data.TYPES));
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            }
            BakedModel bakedModel = itemRenderer.getModel(stack, blockEntity.getLevel(), null, 0);
            itemRenderer.render(stack, ItemDisplayContext.FIXED, true, poseStack, bufferSource, packedLight, packedOverlay, bakedModel);
        }
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(ManaitaFurnaceBlockEntity blockEntity) {
        return BlockEntityRenderer.super.shouldRenderOffScreen(blockEntity);
    }

    @Override
    public int getViewDistance() {
        return BlockEntityRenderer.super.getViewDistance();
    }

    @Override
    public boolean shouldRender(ManaitaFurnaceBlockEntity blockEntity, Vec3 cameraPos) {
        return BlockEntityRenderer.super.shouldRender(blockEntity, cameraPos);
    }
}