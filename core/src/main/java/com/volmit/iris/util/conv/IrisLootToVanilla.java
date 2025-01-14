package com.volmit.iris.util.conv;

import com.volmit.iris.Iris;
import com.volmit.iris.engine.object.IrisEnchantment;
import com.volmit.iris.engine.object.IrisLoot;
import com.volmit.iris.engine.object.IrisLootTable;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.json.JSONArray;
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
            VanillaLootPoolFunction funcBaseDamage = new VanillaLootPoolFunction();
            VanillaLootPoolFunction funcBaseEnchant = new VanillaLootPoolFunction();
            funcs.add(funcBaseCount.setCount(
                new VanillaLootPoolFunctionCount(
                    loot.getMinAmount(),
                    loot.getMaxAmount()
                )
            ));

            double durMax = loot.getMaxDurability();
            double durMin = loot.getMinDurability();
            if (Double.compare(durMax, 1) != 0 || Double.compare(durMin, 0) != 0) {
                funcs.add(funcBaseDamage.setDamage(
                    new VanillaLootPoolFunctionDamage(
                        1d-loot.getMaxDurability(),
                        1d-loot.getMinDurability()
                    )
                ));
            }

            KList<IrisEnchantment> irisEnchantments = loot.getEnchantments();
            if (irisEnchantments.size() > 0) {
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
        VanillaLoot vanillaLoot = new VanillaLoot(lootType, String.format("%ss/%s", lootType, fileName), pools);

        return vanillaLoot.toJson();
    }
}