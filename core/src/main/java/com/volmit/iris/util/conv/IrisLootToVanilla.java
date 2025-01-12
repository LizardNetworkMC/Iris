package com.volmit.iris.util.conv;

import com.volmit.iris.engine.object.IrisLoot;
import com.volmit.iris.engine.object.IrisLootTable;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.json.JSONArray;
import com.volmit.iris.util.json.JSONObject;

public class IrisLootToVanilla {
    public static String toJson(IrisLootTable table, String fileName) {
        KList<VanillaLootPoolEntry> entries = new KList<>();

        int maxRarity = 0;
        KList<IrisLoot> irisLoot = table.getLoot();
        for (IrisLoot loot : irisLoot) {
            maxRarity = Math.max(maxRarity, loot.getRarity());
        }

        for (IrisLoot loot : irisLoot) {
            String name = loot.getType();
            if (!name.contains(":")) {
                name = ":minecraft" + name;
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
            funcs.add(funcBaseDamage.setDamage(
                new VanillaLootPoolFunctionDamage(
                    1-loot.getMaxDurability(),
                    1-loot.getMinDurability()
                )
            ));
            funcs.add(funcBaseEnchant.setEnchant(
                new VanillaLootPoolFunctionEnchant(
                    "minecraft:enchant_randomly",
                    "#minecraft:on_random_loot"
                )
            ));
            entries.add(new VanillaLootPoolEntry(
                name,
                maxRarity-loot.getRarity(),
                funcs
            ));
        }

        JSONObject json = new JSONObject();
        String lootType = "minecraft:chest";
        KList<VanillaLootPool> pools = new KList<>();
        pools.add(new VanillaLootPool(entries));
        VanillaLoot vanillaLoot = new VanillaLoot(lootType, String.format("%s/%s", lootType, fileName), pools);

        return vanillaLoot.toJson();
    }
}