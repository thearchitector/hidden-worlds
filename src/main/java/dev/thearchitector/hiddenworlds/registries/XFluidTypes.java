package dev.thearchitector.hiddenworlds.registries;

import dev.thearchitector.hiddenworlds.HiddenWorlds;
import dev.thearchitector.hiddenworlds.fluids.types.HoneyFluidType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class XFluidTypes {

    private static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(
        ForgeRegistries.Keys.FLUID_TYPES, HiddenWorlds.MODID);
    public static final RegistryObject<FluidType> LIQUID_HONEY = FLUID_TYPES.register(
        "liquid_honey", () -> new HoneyFluidType(
            FluidType.Properties.create().descriptionId("block.hiddenworlds.liquid_honey")
                                .canSwim(false).canDrown(true).canPushEntity(false)
                                .fallDistanceModifier(0.75F).pathType(BlockPathTypes.STICKY_HONEY)
                                .adjacentPathType(null)
                                .sound(SoundActions.BUCKET_FILL, SoundEvents.HONEY_BLOCK_FALL)
                                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.HONEY_BLOCK_FALL)
                                .density(1450).viscosity(6000).rarity(Rarity.EPIC)));

    public static void registerAll(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
