package com.volmit.iris.util.conv;

import java.util.Random;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.framework.PlacedObject;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.conv.VanillaLootPool;
import com.volmit.iris.util.json.JSONArray;
import com.volmit.iris.util.json.JSONObject;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.Location;
import org.bukkit.loot.Lootable;
import org.bukkit.loot.LootTable;
import org.bukkit.NamespacedKey;

public record VanillaLoot(String type, String randomSequence, KList<VanillaLootPool> pools) {
    public String toJson() {
        JSONObject json = new JSONObject();
        json.put("type", this.type());

        JSONArray pools = new JSONArray();
        for (VanillaLootPool pool : this.pools) {
            pools.put(pool.toJSONObject());
        }
        json.put("pools", pools);
        json.put("random_sequence", this.randomSequence());

        return json.toString(4);
    }

    public static boolean setVanillaLootTable(Block originBlock, PlacedObject placed) {
        if (placed == null || placed.getPlacement() == null) {
            return false;
        }

        String[] tableNames = placed.getPlacement().getVanillaLootTableName();
        for (String name : tableNames) {
            NamespacedKey key = NamespacedKey.fromString(name);
            setLootTable(key, originBlock.getLocation());
        }

        return true;
    }

    private static void setLootTable(NamespacedKey lootTableKey, Location loc) {
        Block blk = loc.getBlock();
        if (!(blk.getState() instanceof Lootable)) {
            return;
        }

        // Get the loot table by its name and set the block data accordingly.
        LootTable lootTable = Bukkit.getLootTable(lootTableKey);
        Lootable lootable = (Lootable) blk.getState();
        lootable.setLootTable(lootTable);
        lootable.setSeed(new Random().nextLong());

        // Finally update the container.
        Container container = (Container) lootable;
        container.update();
    }
}
