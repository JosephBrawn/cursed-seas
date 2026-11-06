package com.cursedseas.mod.registry;

import com.cursedseas.mod.CursedSeas;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.cursedseas.mod.item.GoldDoubloonItem;

public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(CursedSeas.MODID);

    public static final DeferredItem<GoldDoubloonItem> GOLD_DOUBLOON =
            ITEMS.registerItem(
                    "gold_doubloon",
                    GoldDoubloonItem::new
            );
}
