
package com.cursedseas.mod.client.event;

import com.cursedseas.mod.CursedSeas;
import com.cursedseas.mod.client.screen.PirateInventoryScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(
        modid = CursedSeas.MODID,
        value = Dist.CLIENT
)
public final class PirateInventoryOpenEvents {

    private PirateInventoryOpenEvents() {
    }


    @SubscribeEvent
    public static void onScreenOpening(ScreenEvent.Opening event) {

        if (event.getNewScreen() == null) {
            return;
        }

        CursedSeas.LOGGER.info(
                "[CursedSeas] Opening screen: {}",
                event.getNewScreen().getClass().getName()
        );

        if (event.getNewScreen().getClass() != InventoryScreen.class) {
            return;
        }

        var player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

        event.setNewScreen(new PirateInventoryScreen(player));

        CursedSeas.LOGGER.info(
                "[CursedSeas] Pirate inventory screen installed!"
        );
    }

}
