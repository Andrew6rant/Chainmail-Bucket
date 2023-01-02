package io.github.andrew6rant.chainmail_bucket;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ChainmailBucket implements ModInitializer {
    public static final ArmorMaterial CUSTOM_CHAINMAIL_ARMOR_MATERIAL = new ChainmailBucketArmorMaterial();

    public static final ChainmailBucketItem CHAINMAIL_BUCKET = new ChainmailBucketItem(CUSTOM_CHAINMAIL_ARMOR_MATERIAL, EquipmentSlot.HEAD, new Item.Settings());


    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, new Identifier("chainmail_bucket", "chainmail_bucket"), CHAINMAIL_BUCKET);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.addAfter(Items.BUCKET, CHAINMAIL_BUCKET));
    }
}
