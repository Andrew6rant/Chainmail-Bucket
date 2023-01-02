package io.github.andrew6rant.chainmail_bucket;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ChainmailBucket implements ModInitializer {

    public static final ChainmailBucketItem CHAINMAIL_BUCKET = new ChainmailBucketItem(new Item.Settings().maxDamage(238));


    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, new Identifier("chainmail_bucket", "chainmail_bucket"), CHAINMAIL_BUCKET);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.addAfter(Items.BUCKET, CHAINMAIL_BUCKET));
    }
}
