package dev.thearchitector.hiddenworlds.registries;

import com.mojang.serialization.Codec;
import dev.thearchitector.hiddenworlds.HiddenWorlds;
import dev.thearchitector.hiddenworlds.features.tree_decorators.AlterUnevenGroundDecorator;
import dev.thearchitector.hiddenworlds.features.trunks.ToweringTrunkPlacer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class XTreeParts {
    public static void registerAll(IEventBus eventBus) {
        TrunkPlacers.TRUNK_PLACER_TYPES.register(eventBus);
        TreeDecorators.TREE_DECORATORS.register(eventBus);
    }

    public abstract static class TrunkPlacers {
        private static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACER_TYPES =
                DeferredRegister.create(
                        Registries.TRUNK_PLACER_TYPE, HiddenWorlds.MODID);

        public static final RegistryObject<TrunkPlacerType<ToweringTrunkPlacer>> TOWERING_TRUNK_PLACER = registerTrunkPlacer(
                "towering_trunk_placer", ToweringTrunkPlacer.CODEC);

        private static <T extends TrunkPlacer> RegistryObject<TrunkPlacerType<T>> registerTrunkPlacer(
                String id, Codec<T> codec
        ) {
            return TRUNK_PLACER_TYPES.register(id, () -> new TrunkPlacerType<>(codec));
        }
    }

    public abstract static class TreeDecorators {
        private static final DeferredRegister<TreeDecoratorType<?>> TREE_DECORATORS =
                DeferredRegister.create(
                        ForgeRegistries.TREE_DECORATOR_TYPES, HiddenWorlds.MODID);

        public static final RegistryObject<TreeDecoratorType<AlterUnevenGroundDecorator>> UNEVEN_ALTER_GROUND_DECORATOR = registerTreeDecorator(
                "alter_uneven_ground", AlterUnevenGroundDecorator.CODEC
        );

        private static <T extends TreeDecorator> RegistryObject<TreeDecoratorType<T>> registerTreeDecorator(String id, Codec<T> codec) {
            return TREE_DECORATORS.register(id, () -> new TreeDecoratorType<>(codec));
        }
    }
}
