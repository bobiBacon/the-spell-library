package net.bobbacon.block;

import com.google.common.collect.ImmutableMap;
import net.bobbacon.block.entity.Decryptor;
import net.bobbacon.block.entity.ModBEs;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class DecryptorBlock extends BlockWithEntity {
    public static final BooleanProperty DECRYPTED = BooleanProperty.of("decrypted");
    private static final VoxelShape BASE_SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 1.0, 15.0);
    private static final VoxelShape STEP_SHAPE = Block.createCuboidShape(2.0, 1.0, 2.0, 14.0, 2.0, 14.0);
    private static final VoxelShape STEM_SHAPE = Block.createCuboidShape(3.0, 2.0, 3.0, 13.0, 13.0, 13.0);
//    private static final VoxelShape STEM_UP_SHAPE_NORTH = VoxelShapes.union(Block.createCuboidShape(3.0, 10.0, 5.0, 13.0, 11.0, 9.0),Block.createCuboidShape(3,10,9,13,13,13));
//    private static final VoxelShape STEM_UP_SHAPE_EAST = VoxelShapes.union(Block.createCuboidShape(5.0, 10.0, 3.0, 9.0, 11.0, 13.0),Block.createCuboidShape(3,10,9,13,13,13));
//    private static final VoxelShape STEM_UP_SHAPE_SOUTH = VoxelShapes.union(Block.createCuboidShape(3.0, 10.0, 5.0, 13.0, 11.0, 9.0),Block.createCuboidShape(3,10,9,13,13,13));
//    private static final VoxelShape STEM_UP_SHAPE_WEST = VoxelShapes.union(Block.createCuboidShape(3.0, 10.0, 5.0, 13.0, 11.0, 9.0),Block.createCuboidShape(3,10,9,13,13,13));



    protected DecryptorBlock(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new Decryptor(pos,state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBEs.DECRYPTOR, Decryptor::tick);
    }
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof Decryptor be){
            return be.onUse(state, world, pos,  player,  hand,  hit);
        }
        return ActionResult.PASS;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.union(BASE_SHAPE,STEM_SHAPE,STEP_SHAPE);

    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(Properties.HORIZONTAL_FACING,DECRYPTED);
    }
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite()).with(DECRYPTED,false);
    }

}
