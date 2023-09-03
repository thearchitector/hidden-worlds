package dev.thearchitector.hiddenworlds.registries;

import com.mojang.serialization.Codec;
import dev.thearchitector.hiddenworlds.HiddenWorlds;
import dev.thearchitector.hiddenworlds.features.trunks.ToweringTrunkPlacer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class XTrees {

    private static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACER_TYPES =
        DeferredRegister.create(
        Registries.TRUNK_PLACER_TYPE, HiddenWorlds.MODID);

    public static final RegistryObject<TrunkPlacerType<ToweringTrunkPlacer>> TOWERING_TRUNK_PLACER = registerTrunkPlacer(
        "towering_trunk_placer", ToweringTrunkPlacer.CODEC);

    public static void registerAll(IEventBus eventBus) {
        TRUNK_PLACER_TYPES.register(eventBus);
    }

    private static <T extends TrunkPlacer> RegistryObject<TrunkPlacerType<T>> registerTrunkPlacer(
        String id, Codec<T> codec
    ) {
        return TRUNK_PLACER_TYPES.register(id, () -> new TrunkPlacerType<>(codec));
    }
}
