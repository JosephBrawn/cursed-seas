
package com.cursedseas.mod.event;

import com.cursedseas.mod.CursedSeas;
import com.cursedseas.mod.registry.ModItems;

import net.minecraft.util.TriState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;

@EventBusSubscriber(modid = CursedSeas.MODID)
public final class DoubloonPickupEvents {

    private DoubloonPickupEvents() {
    }

    @SubscribeEvent
    public static void onItemPickup(ItemEntityPickupEvent.Pre event) {

        if (event.getItemEntity().getItem()
                .is(ModItems.GOLD_DOUBLOON.get())) {

            event.setCanPickup(TriState.FALSE);
        }
    }
}
