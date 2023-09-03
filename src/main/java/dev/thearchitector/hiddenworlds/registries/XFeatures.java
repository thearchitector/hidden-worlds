package dev.thearchitector.hiddenworlds.registries;

import dev.thearchitector.hiddenworlds.HiddenWorlds;
import dev.thearchitector.hiddenworlds.features.TopLayerFeature;
import dev.thearchitector.hiddenworlds.features.ToweringTreeFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class XFeatures {

    public static final RegistryObject<TopLayerFeature> TOP_LAYER;
    public static final RegistryObject<Feature<TreeConfiguration>> TOWERING_TREE;
    private static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(
        ForgeRegistries.FEATURES, HiddenWorlds.MODID);

    public static void registerAll(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }

    static {
        TOP_LAYER = FEATURES.register(
            "top_layer", () -> new TopLayerFeature(ReplaceBlockConfiguration.CODEC));
        TOWERING_TREE = FEATURES.register(
            "towering_tree_8", () -> new ToweringTreeFeature(TreeConfiguration.CODEC, 8));
    }
}
