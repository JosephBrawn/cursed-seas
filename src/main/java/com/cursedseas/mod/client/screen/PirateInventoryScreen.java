
package com.cursedseas.mod.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;
import com.cursedseas.mod.CursedSeas;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.inventory.Slot;

public class PirateInventoryScreen extends InventoryScreen {

    private static final Identifier PIRATE_TEXTURE =
            Identifier.fromNamespaceAndPath(
                    CursedSeas.MODID,
                    "textures/gui/pirate_inventory.png"
            );

    public PirateInventoryScreen(Player player) {
        super(player);

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderLabels(
            GuiGraphics graphics,
            int mouseX,
            int mouseY
    ) {
        super.renderLabels(graphics, mouseX, mouseY);

        graphics.drawString(
                this.font,
                "CURSED SEAS TEST",
                95,
                6,
                0xFFFFAA00,
                false
        );
    }


    @Override
    protected void renderBg(
            GuiGraphics graphics,
            float partialTick,
            int mouseX,
            int mouseY
    ) {



        // Рисуем наш фон вместо ванильного


        graphics.blit(
                RenderPipelines.GUI_TEXTURED,
                PIRATE_TEXTURE,
                this.leftPos - 12,
                this.topPos - 14,
                0.0F,
                0.0F,
                230,
                198,
                1024,
                880,
                1024,
                880
        );



        for (Slot slot : this.menu.slots) {
            int x = this.leftPos + slot.x;
            int y = this.topPos + slot.y;

            // Подсвечиваем реальные области слотов
            graphics.fill(
                    x,
                    y,
                    x + 16,
                    y + 16,
                    0x6600FF00
            );
        }



        // Сохраняем отображение 3D-модели игрока
        if (this.minecraft != null && this.minecraft.player != null) {
            renderEntityInInventoryFollowsMouse(
                    graphics,
                    this.leftPos + 26,
                    this.topPos + 8,
                    this.leftPos + 75,
                    this.topPos + 78,
                    30,
                    0.0625F,
                    mouseX,
                    mouseY,
                    this.minecraft.player
            );
        }
    }

}
