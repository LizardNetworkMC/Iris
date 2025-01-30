/*
 * Iris is a World Generator for Minecraft Bukkit Servers
 * Copyright (c) 2025 xIRoXaSx
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Changes (YYYY-MM-DD):
 *  - 2025-01-23 @xIRoXaSx: Added file.
 */

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

    public static boolean setVanillaLootTable(Block originBlock, PlacedObject placed, String namespace) {
        if (placed == null || placed.getPlacement() == null) {
            return false;
        }

        String[] tableNames = placed.getPlacement().getVanillaLootTableName();
        for (String name : tableNames) {
            // We need to replace the namespace to match our converted loot tables.
            NamespacedKey key = NamespacedKey.fromString(name.replaceFirst("^minecraft", namespace));
            setLootTable(key, originBlock.getLocation());
        }

        return tableNames.length > 0;
    }

    public static void setLootTable(NamespacedKey lootTableKey, Location loc) {
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
