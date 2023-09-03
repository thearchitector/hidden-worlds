package dev.thearchitector.hiddenworlds.features.trunks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.thearchitector.hiddenworlds.registries.XTrees;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.CherryTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class ToweringTrunkPlacer extends CherryTrunkPlacer {

    public static final Codec<ToweringTrunkPlacer> CODEC = RecordCodecBuilder.create(
        (pInstance) -> {
            return trunkPlacerParts(pInstance).and(pInstance.group(IntProvider.codec(1, 3).fieldOf(
                "branch_count").forGetter((toweringTrunkPlacer) -> {
                return toweringTrunkPlacer.branchCount;
            }), IntProvider.codec(2, 16).fieldOf("branch_horizontal_length")
                           .forGetter((toweringTrunkPlacer) -> {
                               return toweringTrunkPlacer.branchHorizontalLength;
                           }), IntProvider.codec(-16, 0, BRANCH_START_CODEC)
                                          .fieldOf("branch_start_offset_from_top")
                                          .forGetter((toweringTrunkPlacer) -> {
                                              return toweringTrunkPlacer.branchStartOffsetFromTop;
                                          }), IntProvider.codec(-16, 16)
                                                         .fieldOf("branch_end_offset_from_top")
                                                         .forGetter((toweringTrunkPlacer) -> {
                                                             return toweringTrunkPlacer.branchEndOffsetFromTop;
                                                         }))).apply(
                pInstance, ToweringTrunkPlacer::new);
        });

    public int scaleFactor;

    public ToweringTrunkPlacer(
        int baseHeight, int heightRandA, int heightRandB, IntProvider branchCount,
        IntProvider branchHorizontalLength, UniformInt branchStartOffsetFromTop,
        IntProvider branchEndOffsetFromTop
    ) {
        super(baseHeight, heightRandA, heightRandB, branchCount, branchHorizontalLength,
            branchStartOffsetFromTop, branchEndOffsetFromTop
        );
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return XTrees.TOWERING_TRUNK_PLACER.get();
    }

    public List<FoliagePlacer.FoliageAttachment> placeTrunk(
        LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter,
        RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, TreeConfiguration pConfig
    ) {
        int i = Math.max(0, pFreeTreeHeight - 1 + this.branchStartOffsetFromTop.sample(pRandom));
        int j = Math.max(
            0, pFreeTreeHeight - 1 + this.secondBranchStartOffsetFromTop.sample(pRandom));
        if (j >= i) {
            ++j;
        }

        int k = this.branchCount.sample(pRandom);
        boolean flag = k == 3;
        boolean flag1 = k >= 2;
        int l;
        if (flag) {
            l = pFreeTreeHeight;
        }
        else if (flag1) {
            l = Math.max(i, j) + 1;
        }
        else {
            l = i + 1;
        }

        // backfill each column of the trunk to soil level
        BlockPos.MutableBlockPos targetPos = new BlockPos.MutableBlockPos();

        for (int x = 0; x < this.scaleFactor; x++) {
            for (int z = 0; z < this.scaleFactor; z++) {
                // we already know the origin block is on solid ground
                if (x == 0 && z == 0) {
                    setDirtAt(pLevel, pBlockSetter, pRandom, pPos.below(), pConfig);
                    continue;
                }

                targetPos.setWithOffset(pPos, x, 0, z);

                while (true) {
                    boolean isHovering = this.validTreePos(pLevel, targetPos);

                    if (!isHovering) {
                        setDirtAt(pLevel, pBlockSetter, pRandom, targetPos, pConfig);
                        break;
                    }

                    pBlockSetter.accept(
                        targetPos, pConfig.trunkProvider.getState(pRandom, targetPos));
                    targetPos.move(Direction.DOWN);
                }
            }
        }

        for (int i1 = 0; i1 < l; ++i1) {
            this.placeLog(
                pLevel, pBlockSetter, pRandom, pPos.above(i1 * this.scaleFactor), pConfig);
        }

        List<FoliagePlacer.FoliageAttachment> list = new ArrayList<>();
        //        if (flag) {
        //            list.add(new FoliagePlacer.FoliageAttachment(pPos.above(l * this
        //            .scaleFactor), 0, false));
        //        }

        BlockPos.MutableBlockPos branchOriginPos = new BlockPos.MutableBlockPos();
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(pRandom);
        Function<BlockState, BlockState> function = (blockState) -> blockState.trySetValue(
            RotatedPillarBlock.AXIS, direction.getAxis());

        //list.add(
        this.generateBranch(pLevel, pBlockSetter, pRandom, pFreeTreeHeight, pPos, pConfig, function,
            direction, i, i < l - 1, branchOriginPos
        );
        //);

        if (flag1) {
            //list.add(
            this.generateBranch(pLevel, pBlockSetter, pRandom, pFreeTreeHeight, pPos, pConfig,
                function, direction.getOpposite(), j, j < l - 1, branchOriginPos
            );
            //);
        }

        return list;
    }

    private FoliagePlacer.FoliageAttachment generateBranch(
        LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter,
        RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, TreeConfiguration pConfig,
        Function<BlockState, BlockState> pPropertySetter, Direction pDirection,
        int pSecondBranchStartOffsetFromTop, boolean p_272719_, BlockPos.MutableBlockPos pCurrentPos
    ) {
        int i = pFreeTreeHeight - 1 + this.branchEndOffsetFromTop.sample(pRandom);
        boolean flag = p_272719_ || i < pSecondBranchStartOffsetFromTop;
        int j = this.branchHorizontalLength.sample(pRandom) + (flag ? 1 : 0);

        pCurrentPos.set(pPos).move(
            Direction.UP, pSecondBranchStartOffsetFromTop * this.scaleFactor);
        BlockPos blockpos = pPos.relative(pDirection, j * this.scaleFactor).above(
            i * this.scaleFactor);
        int k = flag ? 2 : 1;

        for (int l = 0; l < k; ++l) {
            this.placeLog(pLevel, pBlockSetter, pRandom,
                pCurrentPos.move(pDirection, this.scaleFactor), pConfig, pPropertySetter
            );
        }

        Direction direction = blockpos.getY() > pCurrentPos.getY() ? Direction.UP : Direction.DOWN;

        while (true) {
            int i1 = pCurrentPos.distManhattan(blockpos);
            if (i1 == 0) {
                return new FoliagePlacer.FoliageAttachment(
                    blockpos.above(this.scaleFactor), 0, false);
            }

            float f = (float)Math.abs(blockpos.getY() - pCurrentPos.getY()) / (float)i1;
            boolean flag1 = pRandom.nextFloat() < f;
            pCurrentPos.move(flag1 ? direction : pDirection, this.scaleFactor);
            this.placeLog(pLevel, pBlockSetter, pRandom, pCurrentPos, pConfig,
                flag1 ? Function.identity() : pPropertySetter
            );
        }
    }

    @Override
    protected boolean placeLog(
        LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter,
        RandomSource pRandom, BlockPos pPos, TreeConfiguration pConfig,
        Function<BlockState, BlockState> pPropertySetter
    ) {
        ArrayList<BlockPos> appliedPositions = new ArrayList<>(
            this.scaleFactor * this.scaleFactor * this.scaleFactor);

        BlockPos.MutableBlockPos targetPos = new BlockPos.MutableBlockPos();

        for (int x = 0; x < this.scaleFactor; x++) {
            for (int y = 0; y < this.scaleFactor; y++) {
                for (int z = 0; z < this.scaleFactor; z++) {
                    targetPos.setWithOffset(pPos, x, y, z);

                    //                    if (!this.validTreePos(pLevel, targetPos)) {
                    //                        return false;
                    //                    }

                    pBlockSetter.accept(targetPos,
                        pPropertySetter.apply(pConfig.trunkProvider.getState(pRandom, targetPos))
                    );
                    //                    appliedPositions.add(targetPos.immutable());
                }
            }
        }

        //        for (BlockPos pos : appliedPositions) {
        //            pBlockSetter.accept(
        //                pos, pPropertySetter.apply(pConfig.trunkProvider.getState(pRandom, pos)));
        //        }

        return true;
    }

    protected boolean validTreePos(LevelSimulatedReader pLevel, BlockPos pPos) {
        return pLevel.isStateAtPosition(pPos, (state) -> {
            return state.isAir() || (
                state.is(BlockTags.REPLACEABLE_BY_TREES) && !state.is(Blocks.OAK_LOG) && !state.is(
                    Blocks.OAK_LEAVES)
            );
        });
    }
}
