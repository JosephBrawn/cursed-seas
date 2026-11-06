
package com.cursedseas.mod.item;

import com.cursedseas.mod.block.entity.DoubloonPileBlockEntity;
import com.cursedseas.mod.registry.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class GoldDoubloonItem extends Item {

    public GoldDoubloonItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onEntityItemUpdate(
            ItemStack stack,
            ItemEntity entity
    ) {
        // Изменяем мир только на сервере
        if (!(entity.level() instanceof ServerLevel level)) {
            return false;
        }

        // Пока это выброшенный предмет, он не исчезает по времени
        entity.setUnlimitedLifetime();

        // Ждём, когда монеты приземлятся
        if (!entity.onGround() || stack.isEmpty()) {
            return false;
        }

        BlockPos pos = entity.blockPosition();

        // Если здесь уже есть кучка, увеличиваем её
        if (level.getBlockEntity(pos)
                instanceof DoubloonPileBlockEntity pile) {

            // Защита от переполнения целого числа
            if (pile.getCoins() > Integer.MAX_VALUE - stack.getCount()) {
                return false;
            }

            pile.setCoins(pile.getCoins() + stack.getCount());
            entity.discard();

            return true;
        }

        // Новую кучку пока создаём только в пустом пространстве
        if (!level.isEmptyBlock(pos)) {
            return false;
        }

        // Воду и другие жидкости обработаем позднее
        if (!level.getFluidState(pos).isEmpty()) {
            return false;
        }

        BlockPos below = pos.below();

        // Под кучкой должна быть подходящая опорная поверхность
        if (!level.getBlockState(below).isFaceSturdy(
                level, below, Direction.UP)) {
            return false;
        }

        // Создаём блок кучки
        boolean placed = level.setBlock(
                pos,
                ModBlocks.DOUBLOON_PILE.get().defaultBlockState(),
                3
        );

        if (!placed) {
            return false;
        }

        // Переносим количество монет в хранилище блока
        if (level.getBlockEntity(pos)
                instanceof DoubloonPileBlockEntity newPile) {

            newPile.setCoins(stack.getCount());

            // Удаляем выброшенный предмет только после сохранения количества
            entity.discard();

            return true;
        }

        // Если хранилище не создалось, отменяем установку
        level.removeBlock(pos, false);
        return false;
    }
}
