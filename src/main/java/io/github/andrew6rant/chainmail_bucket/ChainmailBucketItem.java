package io.github.andrew6rant.chainmail_bucket;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FluidModificationItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

public class ChainmailBucketItem extends Item implements FluidModificationItem {
    public ChainmailBucketItem(Settings settings) {
        super(settings);
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
