package dev.thearchitector.hiddenworlds.features.foliage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.CherryFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class ToweringFoliagePlacer extends CherryFoliagePlacer {
    public static final Codec<ToweringFoliagePlacer> CODEC = RecordCodecBuilder.create((toweringFoliagePlacer) -> {
        return foliagePlacerParts(toweringFoliagePlacer).and(toweringFoliagePlacer.group(IntProvider.codec(4, 16).fieldOf("height").forGetter((p_273527_) -> {
            return p_273527_.height;
        }), Codec.floatRange(0.0F, 1.0F).fieldOf("wide_bottom_layer_hole_chance").forGetter((p_273760_) -> {
            return p_273760_.wideBottomLayerHoleChance;
        }), Codec.floatRange(0.0F, 1.0F).fieldOf("corner_hole_chance").forGetter((p_273020_) -> {
            return p_273020_.wideBottomLayerHoleChance;
        }), Codec.floatRange(0.0F, 1.0F).fieldOf("hanging_leaves_chance").forGetter((p_273148_) -> {
            return p_273148_.hangingLeavesChance;
        }), Codec.floatRange(0.0F, 1.0F).fieldOf("hanging_leaves_extension_chance").forGetter((p_273098_) -> {
            return p_273098_.hangingLeavesExtensionChance;
        }))).apply(toweringFoliagePlacer, ToweringFoliagePlacer::new);
    });
    public int scaleFactor;

    public ToweringFoliagePlacer(IntProvider radius, IntProvider offset,
                                 IntProvider heightProvider, float wideBottomLayerHoleChance,
                                 float cornerHoleChance, float hangingLeavesChance, float hangingLeavesExtensionChance) {
        super(radius, offset, heightProvider, wideBottomLayerHoleChance, cornerHoleChance, hangingLeavesChance, hangingLeavesExtensionChance);
    }

    protected FoliagePlacerType<?> type() {
        return FoliagePlacerType.CHERRY_FOLIAGE_PLACER;
    }

    protected void createFoliage(LevelSimulatedReader pLevel, FoliagePlacer.FoliageSetter pBlockSetter, RandomSource pRandom, TreeConfiguration pConfig, int pMaxFreeTreeHeight, FoliagePlacer.FoliageAttachment pAttachment, int pFoliageHeight, int pFoliageRadius, int pOffset) {
        BlockPos blockpos = pAttachment.pos().above(pOffset);
        int i = pFoliageRadius + pAttachment.radiusOffset() - 1;
        this.placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig, blockpos, i - 2, pFoliageHeight - 3, false);
        this.placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig, blockpos, i - 1, pFoliageHeight - 4, false);

        for(int j = pFoliageHeight - 5; j >= 0; --j) {
            this.placeLeavesRow(pLevel, pBlockSetter, pRandom, pConfig, blockpos, i, j, false);
        }

//        this.placeLeavesRowWithHangingLeavesBelow(pLevel, pBlockSetter, pRandom, pConfig, blockpos, i, -1, false, this.hangingLeavesChance, this.hangingLeavesExtensionChance);
//        this.placeLeavesRowWithHangingLeavesBelow(pLevel, pBlockSetter, pRandom, pConfig, blockpos, i - 1, -2, false, this.hangingLeavesChance, this.hangingLeavesExtensionChance);
    }

    protected void placeLeavesRow(LevelSimulatedReader pLevel, FoliagePlacer.FoliageSetter pFoliageSetter, RandomSource pRandom, TreeConfiguration pTreeConfiguration, BlockPos pPos, int pRange, int pLocalY, boolean pLarge) {
        int scaleOffset = this.scaleFactor;
        int scaleRange = pRange * scaleOffset;

        BlockPos.MutableBlockPos origin = new BlockPos.MutableBlockPos().setWithOffset(pPos, -scaleRange, pLocalY, -scaleRange);
        BlockPos.MutableBlockPos target = new BlockPos.MutableBlockPos().setWithOffset(pPos, scaleRange, pLocalY+scaleOffset-1, scaleRange);

        for(BlockPos pos : BlockPos.betweenClosed(origin, target)) {
            if (!this.shouldSkipLocationSigned(pRandom, pos.getX(), pos.getY(), pos.getZ(), scaleRange, pLarge)) {
                tryPlaceLeaf(pLevel, pFoliageSetter, pRandom, pTreeConfiguration, pos);
            }
        }
    }

//    protected void placeLeavesRowWithHangingLeavesBelow(LevelSimulatedReader pLevel, FoliagePlacer.FoliageSetter pFoliageSetter, RandomSource pRandom, TreeConfiguration pTreeConfiguration, BlockPos pPos, int pRange, int pLocalY, boolean pLarge, float pHangingLeavesChance, float pHangingLeavesExtensionChance) {
//        this.placeLeavesRow(pLevel, pFoliageSetter, pRandom, pTreeConfiguration, pPos, pRange, pLocalY, pLarge);
//        int i = pLarge ? 1 : 0;
//        BlockPos blockpos = pPos.below();
//        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
//
//        for(Direction direction : Direction.Plane.HORIZONTAL) {
//            Direction direction1 = direction.getClockWise();
//            int j = direction1.getAxisDirection() == Direction.AxisDirection.POSITIVE ? pRange + i : pRange;
//            blockpos$mutableblockpos.setWithOffset(pPos, 0, pLocalY - 1, 0).move(direction1, j).move(direction, -pRange);
//            int k = -pRange;
//
//            while(k < pRange + i) {
//                boolean flag = pFoliageSetter.isSet(blockpos$mutableblockpos.move(Direction.UP));
//                blockpos$mutableblockpos.move(Direction.DOWN);
//                if (flag && tryPlaceExtension(pLevel, pFoliageSetter, pRandom, pTreeConfiguration, pHangingLeavesChance, blockpos, blockpos$mutableblockpos)) {
//                    blockpos$mutableblockpos.move(Direction.DOWN);
//                    tryPlaceExtension(pLevel, pFoliageSetter, pRandom, pTreeConfiguration, pHangingLeavesExtensionChance, blockpos, blockpos$mutableblockpos);
//                    blockpos$mutableblockpos.move(Direction.UP);
//                }
//
//                ++k;
//                blockpos$mutableblockpos.move(direction);
//            }
//        }
//
//    }

}
