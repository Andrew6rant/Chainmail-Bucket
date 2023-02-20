package io.github.andrew6rant.chainmail_bucket;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ChainmailBucket implements ModInitializer {
    public static final ArmorMaterial CUSTOM_CHAINMAIL_ARMOR_MATERIAL = new ChainmailBucketArmorMaterial();

    public static final ChainmailBucketItem CHAINMAIL_BUCKET = new ChainmailBucketItem(CUSTOM_CHAINMAIL_ARMOR_MATERIAL, EquipmentSlot.HEAD, new Item.Settings().group(ItemGroup.MISC));


    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, new Identifier("chainmail_bucket", "chainmail_bucket"), CHAINMAIL_BUCKET);
    }
}
