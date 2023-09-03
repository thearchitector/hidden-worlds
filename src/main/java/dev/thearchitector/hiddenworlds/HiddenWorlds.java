package dev.thearchitector.hiddenworlds;

import com.mojang.logging.LogUtils;
import dev.thearchitector.hiddenworlds.registries.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(HiddenWorlds.MODID)
public class HiddenWorlds {

    public static final String MODID = "hiddenworlds";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
        DeferredRegister.create(
        Registries.CREATIVE_MODE_TAB, MODID);

    public HiddenWorlds() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        LOGGER.info("Registering abstract game objects...");
        XFluidTypes.registerAll(modEventBus);
        XFluids.registerAll(modEventBus);

        // objects
        LOGGER.info("Registering game objects...");
        XBlocks.registerAll(modEventBus);
        XItems.registerAll(modEventBus);

        // world gen
        LOGGER.info("Registering world generation references...");
        XFeatures.registerAll(modEventBus);
        XTrees.registerAll(modEventBus);
        
        // misc
        CREATIVE_MODE_TABS.register(MODID,
            () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.hiddenworlds"))
                                 .icon(() -> XItems.HONEY_BUCKET.get().getDefaultInstance())
                                 .displayItems((parameters, output) -> {
                                     output.accept(XItems.HONEY_BUCKET.get());
                                     output.accept(XBlocks.PETRIFIED_HONEYCOMB.get());
                                 }).build()
        );
        CREATIVE_MODE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Events {

        @SubscribeEvent
        static void onClientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(XFluids.LIQUID_HONEY_SOURCE.get(),
                RenderType.translucent()
            );
            ItemBlockRenderTypes.setRenderLayer(XFluids.LIQUID_HONEY_FLOWING.get(),
                RenderType.translucent()
            );
        }

        @SubscribeEvent
        static void onSpawnPlacementRegistration(SpawnPlacementRegisterEvent event) {
            event.register(EntityType.BEE, SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, null
            );
        }
    }
}
