package dev.thearchitector.hiddenworlds.fluids.types;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class CustomFluidType extends FluidType {
    private final ResourceLocation stillTexture, flowingTexture, overlayTexture, submergedTexture;
    private final int tint;
    private final Vector3f fogColor;
    private final Vector2f fogRange;

    public CustomFluidType(
        ResourceLocation stillTexture,
        ResourceLocation flowingTexture,
        ResourceLocation overlayTexture,
        ResourceLocation submergedTexture,
        int tint,
        Vector3f fogColor,
        Vector2f fogRange,
        Properties properties
    ) {
        super(properties);
        this.stillTexture = stillTexture;
        this.flowingTexture = flowingTexture;
        this.overlayTexture = overlayTexture;
        this.submergedTexture = submergedTexture;
        this.tint = tint;
        this.fogColor = fogColor;
        this.fogRange = fogRange;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public int getTintColor() {
                return CustomFluidType.this.tint;
            }

            @Override
            public ResourceLocation getStillTexture() {
                return CustomFluidType.this.stillTexture;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return CustomFluidType.this.flowingTexture;
            }

            @Override
            public ResourceLocation getOverlayTexture() {
                return CustomFluidType.this.overlayTexture;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return CustomFluidType.this.submergedTexture;
            }

            @Override
            @NotNull
            public Vector3f modifyFogColor(
                Camera camera,
                float partialTick,
                ClientLevel level,
                int renderDistance,
                float darkenWorldAmount,
                Vector3f fluidFogColor
            ) {
                return CustomFluidType.this.fogColor;
            }

            @Override
            public void modifyFogRender(
                Camera camera,
                FogRenderer.FogMode mode,
                float renderDistance,
                float partialTick,
                float nearDistance,
                float farDistance,
                FogShape shape
            ) {
                RenderSystem.setShaderFogStart(CustomFluidType.this.fogRange.x);
                RenderSystem.setShaderFogEnd(CustomFluidType.this.fogRange.y);
            }
        });
    }
}
