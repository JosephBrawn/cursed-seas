package com.cursedseas.mod.registry;

import com.cursedseas.mod.CursedSeas;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(CursedSeas.MODID);

    public static final DeferredItem<Item> GOLD_DOUBLOON =
            ITEMS.registerSimpleItem("gold_doubloon");
}
