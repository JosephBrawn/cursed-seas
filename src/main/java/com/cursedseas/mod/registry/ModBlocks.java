
package com.cursedseas.mod.registry;

import com.cursedseas.mod.CursedSeas;
import com.cursedseas.mod.block.DoubloonPileBlock;

import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(CursedSeas.MODID);

    public static final DeferredBlock<DoubloonPileBlock> DOUBLOON_PILE =
            BLOCKS.registerBlock(
                    "doubloon_pile",
                    DoubloonPileBlock::new,
                    properties -> properties
                            .noOcclusion()
                            .strength(-1.0F, 3600000.0F)
            );

    private ModBlocks() {
    }
}
