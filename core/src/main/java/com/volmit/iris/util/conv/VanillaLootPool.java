package com.volmit.iris.util.conv;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.conv.VanillaLootPoolEntry;
import com.volmit.iris.util.json.JSONArray;
import com.volmit.iris.util.json.JSONObject;

public record VanillaLootPool(float bonusRoll, KList<VanillaLootPoolEntry> entries, VanillaLootPoolRoll roll) {
    public VanillaLootPool(KList<VanillaLootPoolEntry> entries, VanillaLootPoolRoll roll) {
        this(0f, entries, roll);
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("bonus_rolls", this.bonusRoll());

        JSONArray entries = new JSONArray();
        for (VanillaLootPoolEntry entry : this.entries) {
            entries.put(entry.toJSONObject());
        }
        json.put("entries", entries);
        json.put("rolls", this.roll().toJSONObject());

        return json;
    }
}