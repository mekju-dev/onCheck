package com.wgu.testing123;

import junit.framework.TestCase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapterTest extends TestCase implements ItemRecyclerViewInterface{

    @Test
    void setData() {
        List<Item> previousList = new ArrayList<Item>();
        List<Item> itemListToSet = new ArrayList<Item>();
        Item item = new Item(150, "Dolly", "2-wheel silver handtruck dolly", 1);
        Item item2 = new Item(160, "Key", "key for delivery van", 1);
        Item item3 = new Item(170, "Battery Pack", "Portable charger with usb port", 1);
        Item item4 = new Item(180, "Cable", "usb-c cable", 1);

        previousList.add(item);
        previousList.add(item2);

        itemListToSet.add(item3);
        itemListToSet.add(item3);
        itemListToSet.add(item4);
        ItemAdapter itemAdapter = new ItemAdapter(previousList, this);
        itemAdapter.setData(itemListToSet);
        Assertions.assertEquals(itemListToSet, itemAdapter.getItemList());
    }

    /**
     * @param item
     */
    @Override
    public void onItemClick(Item item) {

    }

    /**
     * @param item
     */
    @Override
    public void onItemHold(Item item) {

    }
}