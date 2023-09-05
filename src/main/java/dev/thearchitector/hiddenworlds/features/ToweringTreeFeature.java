package dev.thearchitector.hiddenworlds.features;

import com.mojang.serialization.Codec;
import dev.thearchitector.hiddenworlds.features.trunks.ToweringTrunkPlacer;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class ToweringTreeFeature extends TreeFeature {

    private final int scaleFactor;

    public ToweringTreeFeature(
        Codec<TreeConfiguration> pCodec, int scaleFactor
    ) {
        super(pCodec);
        this.scaleFactor = scaleFactor;
    }

    /**
     * Place the tree at the given location.
     * <p>
     * Feature generation enforces a write-limit of a 1x1 ring of chunks around the origin chunk,
     * disallowing any generation that takes place outside these bounds. To accommodate towering
     * trees we must also scale that boundary by the configured scaling factor.
     * <p>
     * This should be done via procedural jigsaw structures instead; this is more of an operation
     * in "can I" rather than "should I."
     *
     * @param pContext A context object with a reference to the level and the position at which the
     *                 feature is being placed.
     * @return
     */
    @Override
    public boolean place(FeaturePlaceContext<TreeConfiguration> pContext) {

        WorldGenRegion worldGenRegion = ((WorldGenRegion)pContext.level());
        int writeCutoff = worldGenRegion.writeRadiusCutoff;
        worldGenRegion.writeRadiusCutoff = this.scaleFactor;
        ((ToweringTrunkPlacer)pContext.config().trunkPlacer).scaleFactor = this.scaleFactor;

        boolean generated = super.place(pContext);

        worldGenRegion.writeRadiusCutoff = writeCutoff;
        return generated;
    }
}
