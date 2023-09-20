package dev.thearchitector.hiddenworlds.features.trunks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.thearchitector.hiddenworlds.HiddenWorlds;
import dev.thearchitector.hiddenworlds.registries.XTreeParts;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
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
        return XTreeParts.TrunkPlacers.TOWERING_TRUNK_PLACER.get();
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
        for(int x = 0; x < this.scaleFactor; ++x) {
            for(int z = 0; z < this.scaleFactor; ++z) {
                if(x == 0 && z == 0) continue;

                targetPos.setWithOffset(pPos, x, -1, z);

                for(int y = targetPos.getY(); this.validTreePos(pLevel, targetPos); targetPos.setY(--y)) {
                    pBlockSetter.accept(targetPos, pConfig.trunkProvider.getState(pRandom, targetPos));
                }

                pBlockSetter.accept(targetPos, pConfig.dirtProvider.getState(pRandom, targetPos));
            }
        }

        // create central trunk
        for (int i1 = 0; i1 < l; ++i1) {
            this.placeLog(
                pLevel, pBlockSetter, pRandom, pPos.above(i1 * this.scaleFactor), pConfig);
        }

        List<FoliagePlacer.FoliageAttachment> foliageAttachments = new ArrayList<>();
        if (flag) {
            foliageAttachments.add(new FoliagePlacer.FoliageAttachment(pPos.above(l * this.scaleFactor), 0, false));
        }

        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(pRandom).getClockWise();
        this.placeBranches(pLevel, pBlockSetter, pRandom, pFreeTreeHeight, pPos, pConfig, i, j, flag1, l, direction, foliageAttachments);

        return foliageAttachments;
    }

    private void placeBranches(
            LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter,
            RandomSource pRandom, int pFreeTreeHeight, BlockPos pPos, TreeConfiguration pConfig,
            int topBranchOffset, int secondBranchOffset, boolean atLeastTwoBranches, int treeHeight,
            Direction direction, List<FoliagePlacer.FoliageAttachment> foliageAttachments
    ) {
        BlockPos.MutableBlockPos branchOriginPos = new BlockPos.MutableBlockPos();
        Function<BlockState, BlockState> function = (blockState) -> blockState.trySetValue(RotatedPillarBlock.AXIS, direction.getAxis());

        foliageAttachments.add(
                this.generateBranch(pLevel, pBlockSetter, pRandom, pFreeTreeHeight, pPos, pConfig, function,
                        direction, topBranchOffset, topBranchOffset < treeHeight - 1, branchOriginPos
                )
        );

        if (atLeastTwoBranches) {
            foliageAttachments.add(
                    this.generateBranch(pLevel, pBlockSetter, pRandom, pFreeTreeHeight, pPos, pConfig,
                            function, direction.getOpposite(), secondBranchOffset, secondBranchOffset < treeHeight - 1, branchOriginPos
                    )
            );
        }
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
                return new FoliagePlacer.FoliageAttachment(blockpos.above(this.scaleFactor), 0, false);
            }

            float f = (float)Math.abs(blockpos.getY() - pCurrentPos.getY()) / (float)i1;
            boolean flag1 = pRandom.nextFloat() < f;
            pCurrentPos.move(flag1 ? direction : pDirection, this.scaleFactor);
            this.placeLog(pLevel, pBlockSetter, pRandom, pCurrentPos, pConfig,
                flag1 ? Function.identity() : pPropertySetter
            );
        }
    }

    /**
     * Fill the scaled log position using the provided block and property setters. If a single block within
     * the scaled area cannot be placed, the entire area is not filled.
     *
     * @param pLevel
     * @param pBlockSetter
     * @param pRandom
     * @param pPos
     * @param pConfig
     * @param pPropertySetter
     */
    @Override
    protected boolean placeLog(
        LevelSimulatedReader pLevel, BiConsumer<BlockPos, BlockState> pBlockSetter,
        RandomSource pRandom, BlockPos pPos, TreeConfiguration pConfig,
        Function<BlockState, BlockState> pPropertySetter
    ) {
        ArrayList<BlockPos> targets = new ArrayList<>(this.scaleFactor * this.scaleFactor * this.scaleFactor);
        int scaleOffset = this.scaleFactor - 1;

        for (BlockPos targetPos : BlockPos.betweenClosed(
                pPos, pPos.offset(scaleOffset, scaleOffset, scaleOffset))
        ) {
            // logs can generate through other logs
            if (!this.isFree(pLevel, pPos)) return false;
            targets.add(targetPos.immutable());
        }

        for(BlockPos target : targets) {
            pBlockSetter.accept(target, pPropertySetter.apply(pConfig.trunkProvider.getState(pRandom, target)));
        }

        return true;
    }
}
