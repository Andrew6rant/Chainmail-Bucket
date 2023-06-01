package io.github.andrew6rant.chainmail_bucket.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import static io.github.andrew6rant.chainmail_bucket.ChainmailBucket.CHAINMAIL_BUCKET;

@Environment(EnvType.CLIENT)
public class ChainmailBucketClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModelPredicateProviderRegistry.register(CHAINMAIL_BUCKET, new Identifier("is_helmet"), (itemStack, clientWorld, livingEntity, j) -> {
            if (livingEntity instanceof PlayerEntity) {
                return livingEntity.getEquippedStack(EquipmentSlot.HEAD) == itemStack ? 1.0F : 0.0F;
            } return 0.0F;
        });
    }
}
