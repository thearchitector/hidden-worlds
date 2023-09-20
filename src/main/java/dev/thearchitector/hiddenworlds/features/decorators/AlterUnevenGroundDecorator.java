package dev.thearchitector.hiddenworlds.features.decorators;

import com.mojang.serialization.Codec;
import dev.thearchitector.hiddenworlds.registries.XTreeParts;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.List;

public class AlterUnevenGroundDecorator extends AlterGroundDecorator {
    public static final Codec<AlterUnevenGroundDecorator> CODEC = BlockStateProvider.CODEC.fieldOf("provider")
                                                                                    .xmap(AlterUnevenGroundDecorator::new, (alterUnevenGroundDecorator) -> {
        return alterUnevenGroundDecorator.provider;
    }).codec();

    public AlterUnevenGroundDecorator(BlockStateProvider blockStateProvider) {
        super(blockStateProvider);
    }

    protected TreeDecoratorType<?> type() {
        return XTreeParts.TreeDecorators.UNEVEN_ALTER_GROUND_DECORATOR.get();
    }

    public void place(TreeDecorator.Context pContext) {
        List<BlockPos> logs = pContext.logs();
        LevelSimulatedReader level = pContext.level();
        if (!logs.isEmpty()) {
            logs.stream().filter((log) -> {
                return level.isStateAtPosition(log.below(), Feature::isDirt);
            }).forEach((log) -> {
                this.placeCircle(pContext, log.west().north());
                this.placeCircle(pContext, log.east(2).north());
                this.placeCircle(pContext, log.west().south(2));
                this.placeCircle(pContext, log.east(2).south(2));

                for(int j = 0; j < 5; ++j) {
                    int k = pContext.random().nextInt(64);
                    int l = k % 8;
                    int i1 = k / 8;
                    if (l == 0 || l == 7 || i1 == 0 || i1 == 7) {
                        this.placeCircle(pContext, log.offset(-3 + l, 0, -3 + i1));
                    }
                }
            });
        }
    }
}
