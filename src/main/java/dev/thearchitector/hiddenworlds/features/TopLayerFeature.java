package dev.thearchitector.hiddenworlds.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;

public class TopLayerFeature extends Feature<ReplaceBlockConfiguration> {

    public TopLayerFeature(Codec<ReplaceBlockConfiguration> pCodec) {
        super(pCodec);
    }

    /**
     * Replaces every block at the top layer of the origin chunk with the block specified
     * matching the first configured target predicate.
     *
     * @param pContext A context object with a reference to the level and the position at which the
     *                 feature is being placed.
     */
    @Override
    public boolean place(FeaturePlaceContext<ReplaceBlockConfiguration> pContext) {
        WorldGenLevel worldgenlevel = pContext.level();
        BlockPos blockpos = pContext.origin();
        ReplaceBlockConfiguration config = pContext.config();
        RandomSource random = pContext.random();
        BlockPos.MutableBlockPos targetPos = new BlockPos.MutableBlockPos();

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int nx = blockpos.getX() + x;
                int nz = blockpos.getZ() + z;
                int y = worldgenlevel.getHeight(Heightmap.Types.WORLD_SURFACE_WG, nx, nz);

                targetPos.set(nx, y - 1, nz);

                for (OreConfiguration.TargetBlockState targetBlockState : config.targetStates) {
                    if (targetBlockState.target.test(
                        worldgenlevel.getBlockState(targetPos), random)) {
                        worldgenlevel.setBlock(targetPos, targetBlockState.state, 2);
                        break;
                    }
                }
            }
        }

        return true;
    }
}
