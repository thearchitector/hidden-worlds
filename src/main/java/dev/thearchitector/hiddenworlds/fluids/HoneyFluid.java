package dev.thearchitector.hiddenworlds.fluids;

import dev.thearchitector.hiddenworlds.blocks.LiquidHoneyBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.LavaFluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.NotNull;

public abstract class HoneyFluid extends SurfaceRules {

    protected static boolean canBeReplacedWith(Fluid fluidIn) {
        return fluidIn instanceof LavaFluid;
    }

    public static class Source extends ForgeFlowingFluid.Source {

        public Source(Properties properties) {
            super(properties);
        }

        @Override
        protected boolean canBeReplacedWith(
            FluidState state, BlockGetter level, BlockPos pos, Fluid fluidIn, Direction direction
        ) {
            return HoneyFluid.canBeReplacedWith(fluidIn);
        }

        @Override
        public void animateTick(
            Level pLevel, @NotNull BlockPos pPos, @NotNull FluidState pState,
            @NotNull RandomSource pRandom
        ) {
            if (pLevel.getBlockState(pPos).getValue(LiquidHoneyBlock.TRANSDIMENSIONAL)) {
                BlockPos blockpos = pPos.above();
                if (pLevel.getBlockState(blockpos).isAir() && !pLevel.getBlockState(blockpos)
                                                                     .isSolidRender(
                                                                         pLevel, blockpos)) {
                    if (pRandom.nextBoolean()) {
                        double d0 = (double)pPos.getX() + pRandom.nextDouble();
                        double d1 = (double)pPos.getY() + 1D;
                        double d2 = (double)pPos.getZ() + pRandom.nextDouble();
                        int hex = PotionUtils.getColor(
                            pRandom.nextBoolean() ? Potions.NIGHT_VISION : Potions.TURTLE_MASTER);
                        double r = (double)(hex >> 16 & 255) / 255D;
                        double g = (double)(hex >> 8 & 255) / 255D;
                        double b = (double)(hex & 255) / 255D;
                        pLevel.addParticle(ParticleTypes.AMBIENT_ENTITY_EFFECT, d0, d1, d2, r, g,
                            b
                        );
                    }
                }
            }
        }
    }

    public static class Flowing extends ForgeFlowingFluid.Flowing {

        public Flowing(Properties properties) {
            super(properties);
        }

        @Override
        protected boolean canBeReplacedWith(
            FluidState state, BlockGetter level, BlockPos pos, Fluid fluidIn, Direction direction
        ) {
            return HoneyFluid.canBeReplacedWith(fluidIn);
        }
    }
}
