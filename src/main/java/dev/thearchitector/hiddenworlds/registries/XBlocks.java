package dev.thearchitector.hiddenworlds.registries;

import dev.thearchitector.hiddenworlds.HiddenWorlds;
import dev.thearchitector.hiddenworlds.blocks.LiquidHoneyBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public abstract class XBlocks {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
        ForgeRegistries.BLOCKS, HiddenWorlds.MODID);

    public static final RegistryObject<LiquidBlock> LIQUID_HONEY = BLOCKS.register("liquid_honey",
        () -> new LiquidHoneyBlock(
            BlockBehaviour.Properties.copy(Blocks.WATER).mapColor(MapColor.COLOR_ORANGE)
                                     .pushReaction(PushReaction.BLOCK))
    );

    public static final RegistryObject<Block> PETRIFIED_HONEYCOMB = BLOCKS.register(
        "petrified_honeycomb", () -> new Block(
            BlockBehaviour.Properties.copy(Blocks.BEDROCK).mapColor(MapColor.COLOR_ORANGE)
                                     .sound(SoundType.CORAL_BLOCK)));

    public static void registerAll(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
