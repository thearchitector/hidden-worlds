package dev.thearchitector.hiddenworlds.fluids.types;

import dev.thearchitector.hiddenworlds.HiddenWorlds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class HoneyFluidType extends CustomFluidType {

    private static final ResourceLocation HONEY_BLOCK_TOP = new ResourceLocation(
        "minecraft:block/honey_block_top");
    private static final ResourceLocation LIQUID_HONEY_FLOWING = new ResourceLocation(
        HiddenWorlds.MODID, "block/honey_flowing");
    private static final ResourceLocation UNDERHONEY = new ResourceLocation(HiddenWorlds.MODID,
        "textures/misc/underhoney.png"
    );

    public HoneyFluidType(Properties properties) {
        super(HONEY_BLOCK_TOP, LIQUID_HONEY_FLOWING, HONEY_BLOCK_TOP, UNDERHONEY, 0xFFFFFFFF,
            new Vector3f(251F / 255F, 193F / 255F, 87F / 255F), new Vector2f(0.5F, 3.5F), properties
        );
    }

    @Override
    public boolean move(
        FluidState state, LivingEntity entity, Vec3 movementVector, double gravity
    ) {
        // move the entity
        entity.moveRelative(0.01F, movementVector);
        entity.move(MoverType.SELF, entity.getDeltaMovement());

        // set the motion factor
        entity.setDeltaMovement(entity.getDeltaMovement().scale(0.5D));

        // apply gravity
        if (!entity.isNoGravity()) {
            entity.setDeltaMovement(entity.getDeltaMovement().add(0.0D, -gravity / 8.0D, 0.0D));
        }

        // ensure you can exit the fluid
        Vec3 vec34 = entity.getDeltaMovement();
        double d8 = entity.getY();
        if (entity.horizontalCollision && entity.isFree(
            vec34.x, vec34.y + (double)0.6F - d8 + d8, vec34.z)) {
            entity.setDeltaMovement(vec34.x, 0.3F, vec34.z);
        }

        return true;
    }

    @Override
    public void setItemMovement(ItemEntity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        entity.setDeltaMovement(vec3.x * 0.9D, vec3.y * 0.85D, vec3.z * 0.9D);
    }
}
