package com.cursedseas.mod.client;

import com.cursedseas.mod.CursedSeas;
import com.cursedseas.mod.registry.ModItems;

import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = CursedSeas.MODID, value = Dist.CLIENT)
public final class GoldDoubloonClient {

    private GoldDoubloonClient() {
    }

    @SubscribeEvent
    public static void registerExtensions(RegisterClientExtensionsEvent event) {
        event.registerItem(new IClientItemExtensions() {

            @Override
            public boolean shouldBobAsEntity(ItemStack stack) {
                return false;
            }

        }, ModItems.GOLD_DOUBLOON.get());
    }
}
