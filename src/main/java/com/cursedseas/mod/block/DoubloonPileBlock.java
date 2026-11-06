package com.cursedseas.mod.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import com.cursedseas.mod.block.entity.DoubloonPileBlockEntity;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import com.cursedseas.mod.block.entity.DoubloonPileBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.Shapes;


public class DoubloonPileBlock extends Block implements EntityBlock {

    private static final MapCodec<DoubloonPileBlock> CODEC =
            simpleCodec(DoubloonPileBlock::new);

    // Временная физическая форма кучки


    private static final double[][] COIN_CENTERS = {
            {8.0, 8.0},
            {11.25, 8.25},
            {5.0, 8.9},
            {8.1, 11.4},
            {8.0, 4.4},
            {11.8, 11.6},
            {4.3, 4.7}
    };

    private static final VoxelShape[] COIN_SHAPES =
            createCoinShapes();

    private static VoxelShape[] createCoinShapes() {
        VoxelShape[] shapes = new VoxelShape[7];
        VoxelShape combined = Shapes.empty();

        for (int i = 0; i < COIN_CENTERS.length; i++) {
            double x = COIN_CENTERS[i][0];
            double z = COIN_CENTERS[i][1];

            combined = Shapes.or(
                    combined,
                    Block.box(
                            x - 1.85, 0, z - 1.7,
                            x + 1.85, 0.5, z + 1.7
                    )
            );

            shapes[i] = combined.optimize();
        }

        return shapes;
    }


    private static final VoxelShape MEDIUM_SHAPE =
            Block.box(2, 0, 2, 14, 3, 14);

    private static final VoxelShape LARGE_SHAPE =
            Block.box(1, 0, 1, 15, 5, 15);


    private VoxelShape getPileShape(BlockState state) {

        int size = state.getValue(SIZE);

        if (size <= 6) {
            return COIN_SHAPES[size];
        }

        if (size == 7) {
            return MEDIUM_SHAPE;
        }

        return LARGE_SHAPE;
    }




    public static final IntegerProperty SIZE =
            IntegerProperty.create("size", 0, 9);


    public DoubloonPileBlock(BlockBehaviour.Properties properties) {
        super(properties);

        this.registerDefaultState(
                this.stateDefinition.any().setValue(SIZE, 0)
        );
    }






    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return getPileShape(state);
    }

    @Override
    protected VoxelShape getCollisionShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            CollisionContext context
    ) {
        return getPileShape(state);
    }


    @Override
    public BlockEntity newBlockEntity(
            BlockPos pos,
            BlockState state
    ) {
        return new DoubloonPileBlockEntity(pos, state);
    }


    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hitResult
    ) {
        return collectCoins(level, pos, player);
    }

    @Override
    protected InteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult
    ) {
        return collectCoins(level, pos, player);
    }

    private InteractionResult collectCoins(
            Level level,
            BlockPos pos,
            Player player
    ) {
        if (player instanceof ServerPlayer serverPlayer
                && level.getBlockEntity(pos)
                instanceof DoubloonPileBlockEntity pile) {

            int collected = pile.takeCoins(serverPlayer);

            if (collected > 0) {
                serverPlayer.displayClientMessage(
                        Component.translatable(
                                "message.cursedseas.coins_collected",
                                collected
                        ),
                        true
                );
            } else if (pile.getCoins() > 0) {
                serverPlayer.displayClientMessage(
                        Component.translatable(
                                "message.cursedseas.inventory_full"
                        ),
                        true
                );
            }
        }

        return InteractionResult.SUCCESS;
    }


    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(SIZE);
    }


    public static int getSizeForCoins(int coins) {

        // Одна монета
        if (coins <= 1) {
            return 0;
        }

        // От 2 до 7 монет:
        // каждая имеет собственную модель
        if (coins <= 7) {
            return coins - 1;
        }

        // Небольшая кучка
        if (coins < 32) {
            return 7;
        }

        // Средняя куча
        if (coins < 96) {
            return 8;
        }

        // Большая куча
        return 9;
    }




}
