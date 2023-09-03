package dev.thearchitector.hiddenworlds.registries;

import dev.thearchitector.hiddenworlds.HiddenWorlds;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class XItems {

    public static final RegistryObject<Item> PETRIFIED_HONEYCOMB;
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
        ForgeRegistries.ITEMS, HiddenWorlds.MODID);
    public static final RegistryObject<Item> HONEY_BUCKET = ITEMS.register("honey_bucket",
        () -> new BucketItem(XFluids.LIQUID_HONEY_SOURCE,
            new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)
        )
    );

    public static void registerAll(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    private static RegistryObject<Item> registerItemFromBlock(RegistryObject<Block> block) {
        return ITEMS.register(block.getId().getPath(),
            () -> new BlockItem(block.get(), new Item.Properties())
        );
    }

    static {
        PETRIFIED_HONEYCOMB = registerItemFromBlock(XBlocks.PETRIFIED_HONEYCOMB);
    }
}
