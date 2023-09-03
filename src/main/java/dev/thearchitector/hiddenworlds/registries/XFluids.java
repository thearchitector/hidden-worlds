package dev.thearchitector.hiddenworlds.registries;

import dev.thearchitector.hiddenworlds.HiddenWorlds;
import dev.thearchitector.hiddenworlds.fluids.HoneyFluid;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class XFluids {

    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(
        ForgeRegistries.FLUIDS, HiddenWorlds.MODID);
    private static final ForgeFlowingFluid.Properties LIQUID_HONEY_PROPS;
    public static final RegistryObject<FlowingFluid> LIQUID_HONEY_SOURCE = FLUIDS.register(
        "liquid_honey_source", () -> new HoneyFluid.Source(XFluids.LIQUID_HONEY_PROPS));
    public static final RegistryObject<FlowingFluid> LIQUID_HONEY_FLOWING = FLUIDS.register(
        "liquid_honey_flowing", () -> new HoneyFluid.Flowing(XFluids.LIQUID_HONEY_PROPS));
    
    public static void registerAll(IEventBus eventBus) {
        FLUIDS.register(eventBus);
    }

    static {
        LIQUID_HONEY_PROPS = new ForgeFlowingFluid.Properties(XFluidTypes.LIQUID_HONEY,
            LIQUID_HONEY_SOURCE, LIQUID_HONEY_FLOWING
        ).slopeFindDistance(3).levelDecreasePerBlock(3).tickRate(50).block(XBlocks.LIQUID_HONEY)
         .bucket(XItems.HONEY_BUCKET);
    }
}
