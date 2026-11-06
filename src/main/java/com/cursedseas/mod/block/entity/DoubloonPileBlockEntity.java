
package com.cursedseas.mod.block.entity;

import com.cursedseas.mod.registry.ModBlockEntities;

import com.cursedseas.mod.registry.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import com.cursedseas.mod.block.DoubloonPileBlock;
import net.minecraft.world.level.block.Block;


public class DoubloonPileBlockEntity extends BlockEntity {

    // Настоящее количество монет в кучке
    private int coins = 0;

    public DoubloonPileBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DOUBLOON_PILE.get(), pos, state);
    }

    // Узнать количество монет
    public int getCoins() {
        return coins;
    }

    // Изменить количество монет

    public void setCoins(int amount) {
        this.coins = Math.max(0, amount);
        setChanged();

        if (level == null || level.isClientSide()) {
            return;
        }

        BlockState state = level.getBlockState(worldPosition);

        if (!(state.getBlock() instanceof DoubloonPileBlock)) {
            return;
        }

        int newSize = DoubloonPileBlock.getSizeForCoins(coins);

        if (state.getValue(DoubloonPileBlock.SIZE) != newSize) {
            level.setBlock(
                    worldPosition,
                    state.setValue(DoubloonPileBlock.SIZE, newSize),
                    Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS
            );
        }
    }


    // Загрузка количества из сохранённого мира
    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.coins = Math.max(0, input.getIntOr("Coins", 0));
    }

    // Сохранение количества в мир
    @Override
    public void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("Coins", this.coins);
    }

    public int takeCoins(ServerPlayer player) {

        if (coins <= 0) {
            return 0;
        }

        int remaining = coins;
        int initialAmount = coins;

        while (remaining > 0) {

            // Передаём монеты порциями не более 64 штук
            int amount = Math.min(remaining, 64);

            ItemStack stack = new ItemStack(
                    ModItems.GOLD_DOUBLOON.get(),
                    amount
            );

            // Minecraft пытается разместить монеты в инвентаре
            player.getInventory().add(stack);

            // Считаем, сколько действительно поместилось
            int transferred = amount - stack.getCount();

            if (transferred == 0) {
                break;
            }

            remaining -= transferred;
        }

        int collected = initialAmount - remaining;

        if (collected > 0) {

            // Сохраняем остаток монет
            setCoins(remaining);

            // Если забрали всё — удаляем пустую кучку
            if (remaining == 0) {
                player.level().removeBlock(worldPosition, false);
            }
        }

        return collected;
    }

}
