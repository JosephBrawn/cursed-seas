
package com.cursedseas.mod.registry;

import com.cursedseas.mod.CursedSeas;
import com.cursedseas.mod.block.entity.DoubloonPileBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>>
            BLOCK_ENTITY_TYPES =
            DeferredRegister.create(
                    Registries.BLOCK_ENTITY_TYPE,
                    CursedSeas.MODID
            );

    public static final Supplier<BlockEntityType<DoubloonPileBlockEntity>>
            DOUBLOON_PILE =
            BLOCK_ENTITY_TYPES.register(
                    "doubloon_pile",
                    () -> new BlockEntityType<>(
                            DoubloonPileBlockEntity::new,
                            false,
                            ModBlocks.DOUBLOON_PILE.get()
                    )
            );

    private ModBlockEntities() {
    }
}
