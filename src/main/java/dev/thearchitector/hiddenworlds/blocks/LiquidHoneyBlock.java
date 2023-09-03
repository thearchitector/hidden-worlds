package dev.thearchitector.hiddenworlds.blocks;

import dev.thearchitector.hiddenworlds.registries.XFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LiquidHoneyBlock extends LiquidBlock {

    public static final BooleanProperty TRANSDIMENSIONAL = BooleanProperty.create(
        "transdimensional");

    public LiquidHoneyBlock(Properties pProperties) {
        super(XFluids.LIQUID_HONEY_SOURCE, pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(TRANSDIMENSIONAL, false));
    }

    protected void createBlockStateDefinition(
        @NotNull StateDefinition.Builder<Block, BlockState> pBuilder
    ) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(TRANSDIMENSIONAL);
    }

    public float getFriction(
        BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity
    ) {
        return entity instanceof ItemEntity ? super.getFriction(state, level, pos, entity) : 1.5F;
    }


    @SuppressWarnings("deprecation")
    @Override
    public void entityInside(
        @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos,
        @NotNull Entity pEntity
    ) {
        /*
        if (pState.getValue(TRANSDIMENSIONAL)) {
            if (pEntity.canChangeDimensions()) {
                pEntity.portalEntrancePos = pPos.immutable();

                if (pLevel instanceof ServerLevel) {
                    MinecraftServer minecraftserver = pLevel.getServer();
                    ResourceKey<Level> resourcekey = pLevel.dimension() == XDimensions.APIARY ?
                                                     Level.OVERWORLD :
                                                     XDimensions.APIARY;
                    ServerLevel destLevel = minecraftserver.getLevel(resourcekey);

                    if (destLevel != null) {
                        pEntity.changeDimension(destLevel);
                    }
                }
            }
        }
        */
        super.entityInside(pState, pLevel, pPos, pEntity);
    }
}
