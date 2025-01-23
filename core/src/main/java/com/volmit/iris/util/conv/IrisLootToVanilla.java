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

import com.volmit.iris.Iris;
import com.volmit.iris.engine.object.IrisEnchantment;
import com.volmit.iris.engine.object.IrisLoot;
import com.volmit.iris.engine.object.IrisLootTable;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.json.JSONObject;

import org.bukkit.Bukkit;

public class IrisLootToVanilla {
    public static String lootDirectoryName() {
        String serverVersion = Bukkit.getServer().getVersion();
        String[] versions = serverVersion.split("\\.");
        String lootTableDirName = "loot_table";

        try {
            lootTableDirName = versions.length < 2
                ? "loot_tables"
                : (versions[0] == "1" && Integer.decode(versions[1]) >= 21)
                    ? "loot_table"
                    : "loot_tables";
        } catch (NumberFormatException ex) {
            Iris.warn("Unable to decode server version, using loot table dir fallback");
        }

        return lootTableDirName;
    }

    public static String toJson(IrisLootTable table, String fileName) {
        KList<VanillaLootPoolEntry> entries = new KList<>();

        int maxRarity = 0;
        KList<IrisLoot> irisLoot = table.getLoot();
        for (IrisLoot loot : irisLoot) {
            maxRarity = Math.max(maxRarity, loot.getRarity());
        }
        // Weight needs to be at least 1.
        maxRarity++;

        for (IrisLoot loot : irisLoot) {
            String name = loot.getType().toString().toLowerCase();
            if (!name.contains(":")) {
                name = "minecraft:" + name;
            }

            KList<VanillaLootPoolFunction> funcs = new KList<>();
            VanillaLootPoolFunction funcBaseCount = new VanillaLootPoolFunction();
            funcs.add(funcBaseCount.setCount(
                new VanillaLootPoolFunctionCount(
                    loot.getMinAmount(),
                    loot.getMaxAmount()
                )
            ));

            double durMax = loot.getMaxDurability();
            double durMin = loot.getMinDurability();
            if (Double.compare(durMax, 1) != 0 || Double.compare(durMin, 0) != 0) {
                VanillaLootPoolFunction funcBaseDamage = new VanillaLootPoolFunction();
                funcs.add(funcBaseDamage.setDamage(
                    new VanillaLootPoolFunctionDamage(
                        1d-loot.getMaxDurability(),
                        1d-loot.getMinDurability()
                    )
                ));
            }

            KList<IrisEnchantment> irisEnchantments = loot.getEnchantments();
            if (irisEnchantments.size() > 0) {
                VanillaLootPoolFunction funcBaseEnchant = new VanillaLootPoolFunction();
                funcs.add(funcBaseEnchant.setEnchant(
                    new VanillaLootPoolFunctionEnchant(
                        "minecraft:enchant_randomly",
                        "#minecraft:on_random_loot",
                        irisEnchantments
                    )
                ));
            }
            entries.add(new VanillaLootPoolEntry(
                name,
                maxRarity-loot.getRarity(),
                funcs
            ));
        }

        JSONObject json = new JSONObject();
        String lootType = "minecraft:chest";
        KList<VanillaLootPool> pools = new KList<>();
        VanillaLootPoolRoll roll = new VanillaLootPoolRoll(table.getMaxPicked(), table.getMinPicked());
        pools.add(new VanillaLootPool(entries, roll));

        VanillaLoot vanillaLoot = new VanillaLoot(
            lootType,
            String.format("%ss/%s", lootType, fileName),
            pools
        );

        return vanillaLoot.toJson();
    }
}
