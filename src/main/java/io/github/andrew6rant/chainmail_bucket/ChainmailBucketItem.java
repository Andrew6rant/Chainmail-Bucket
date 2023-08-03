package io.github.andrew6rant.chainmail_bucket;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.FluidDrainable;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class ChainmailBucketItem extends ArmorItem implements FluidModificationItem {


    public ChainmailBucketItem(ArmorMaterial material, ArmorItem.Type type, Settings settings) {
        super(material, type, settings);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemStack = playerEntity.getStackInHand(hand);
        BlockHitResult blockHitResult = raycast(world, playerEntity, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (blockHitResult.getType() == HitResult.Type.MISS || blockHitResult.getType() != HitResult.Type.BLOCK) {
            return TypedActionResult.pass(itemStack);
        } else {
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getSide();
            BlockPos blockPos2 = blockPos.offset(direction);
            if (world.canPlayerModifyAt(playerEntity, blockPos) && playerEntity.canPlaceOn(blockPos2, direction, itemStack)) {
                BlockState blockState;
                blockState = world.getBlockState(blockPos);
                if (blockState.getBlock() instanceof FluidDrainable fluidDrainable) {
                    ItemStack isDrainable = fluidDrainable.tryDrainFluid(world, blockPos, blockState);
                    if (!isDrainable.isEmpty()) { // if tryDrainFluid succeeds
                        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
                        fluidDrainable.getBucketFillSound().ifPresent((sound) -> playerEntity.playSound(sound, 1.0F, 1.0F));
                        world.emitGameEvent(playerEntity, GameEvent.FLUID_PICKUP, blockPos);
                        itemStack.damage(1, playerEntity, (player) -> player.sendToolBreakStatus(hand));
                        if (!world.isClient) {
                            BlockState fluidState = blockState.getFluidState().getBlockState().with(FluidBlock.LEVEL, 3);
                            BlockPos playerPos = playerEntity.getBlockPos();
                            Direction playerDirection = playerEntity.getHorizontalFacing().getOpposite();
                            if (world.isAir(playerPos)) { // check if flowing water can be placed without destroying anything
                                world.setBlockState(playerPos, fluidState);
                                if (world.isAir(playerPos.offset(playerDirection))) { // attempt to place water behind the player, since flowing water only updates if there are two of them
                                    world.setBlockState(playerPos.offset(playerDirection), fluidState);
                                }
                            } else if (world.isAir(playerPos.offset(Direction.UP))) { // If the player is standing in a block, attempt to place the water above them
                                world.setBlockState(playerPos.offset(Direction.UP), fluidState);
                                if (world.isAir(playerPos.offset(playerDirection).offset(Direction.UP))) {
                                    world.setBlockState(playerPos.offset(playerDirection).offset(Direction.UP), fluidState);
                                }
                            }
                            //world.emitGameEvent(playerEntity, GameEvent.FLUID_PLACE, blockPos);
                        }

                        return TypedActionResult.success(itemStack, world.isClient());
                    }
                }
                return TypedActionResult.fail(itemStack);
            } else {
                return TypedActionResult.fail(itemStack);
            }
        }
    }

    @Override
    public boolean placeFluid(@Nullable PlayerEntity player, World world, BlockPos pos, @Nullable BlockHitResult hitResult) {
        return false;
    }
}
