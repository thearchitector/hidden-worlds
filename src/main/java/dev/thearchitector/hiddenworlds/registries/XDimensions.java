package dev.thearchitector.hiddenworlds.registries;

import dev.thearchitector.hiddenworlds.HiddenWorlds;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class XDimensions {

    public static final ResourceKey<Level> APIARY = ResourceKey.create(Registries.DIMENSION,
        new ResourceLocation(HiddenWorlds.MODID, "apiary")
    );
    public static final ResourceKey<DimensionType> APIARY_TYPE;

    static {
        APIARY_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, APIARY.location());
    }
}
