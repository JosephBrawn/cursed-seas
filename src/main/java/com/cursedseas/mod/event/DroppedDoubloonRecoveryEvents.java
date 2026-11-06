
package com.cursedseas.mod.event;

import com.cursedseas.mod.CursedSeas;
import com.cursedseas.mod.block.entity.DoubloonPileBlockEntity;
import com.cursedseas.mod.registry.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.Comparator;
import java.util.List;

@EventBusSubscriber(modid = CursedSeas.MODID)
public final class DroppedDoubloonRecoveryEvents {

    private DroppedDoubloonRecoveryEvents() {
    }

    @SubscribeEvent
    public static void onRightClickBlock(
            PlayerInteractEvent.RightClickBlock event
    ) {
        // Обрабатываем одно нажатие, только на сервере.
        if (event.getHand() != InteractionHand.MAIN_HAND
                || !(event.getEntity() instanceof ServerPlayer player)
                || !(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        // Игрок должен нажать на верхнюю поверхность.
        if (event.getHitVec().getDirection() != Direction.UP) {
            return;
        }

        BlockPos clickedPos = event.getPos();

        // Обычные кучки уже самостоятельно обрабатывают ПКМ.
        if (level.getBlockEntity(clickedPos)
                instanceof DoubloonPileBlockEntity) {
            return;
        }

        Vec3 hit = event.getHitVec().getLocation();

        // Ищем только выброшенные предметы рядом
        // с точкой нажатия.
        AABB searchArea = new AABB(
                hit.x - 0.75, hit.y - 0.1, hit.z - 0.75,
                hit.x + 0.75, hit.y + 0.7, hit.z + 0.75
        );

        List<ItemEntity> coins = level.getEntitiesOfClass(
                ItemEntity.class,
                searchArea,
                item -> item.isAlive()
                        && item.onGround()
                        && item.getItem().is(
                        ModItems.GOLD_DOUBLOON.get()
                )
                        && (
                        item.blockPosition().equals(clickedPos)
                                || item.blockPosition().below().equals(clickedPos)
                )
        );

        if (coins.isEmpty()) {
            return;
        }

        // Выбираем ближайший к месту нажатия стак.
        coins.sort(
                Comparator.comparingDouble(
                        item -> item.position().distanceToSqr(hit)
                )
        );

        ItemEntity dropped = coins.get(0);

        // Копируем стак, чтобы сначала проверить,
        // сколько монет действительно войдёт в инвентарь.
        ItemStack remainder = dropped.getItem().copy();
        int initial = remainder.getCount();

        player.getInventory().add(remainder);

        int collected = initial - remainder.getCount();

        if (collected > 0) {
            if (remainder.isEmpty()) {
                dropped.discard();
            } else {
                dropped.setItem(remainder);
            }

            player.displayClientMessage(
                    Component.translatable(
                            "message.cursedseas.coins_collected",
                            collected
                    ),
                    true
            );
        } else {
            player.displayClientMessage(
                    Component.translatable(
                            "message.cursedseas.inventory_full"
                    ),
                    true
            );
        }

        // Не выполняем обычное действие с блоком
        // после успешной обработки нажатия.
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }
}
