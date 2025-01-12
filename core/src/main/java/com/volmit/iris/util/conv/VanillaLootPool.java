package com.volmit.iris.util.conv;

import com.google.code.gson.Gson;
import com.google.code.gson.GsonBuilder;
import com.google.code.gson.FieldNamingPolicy;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.conv.VanillaLootPoolEntry;
import com.volmit.iris.util.json.JSONObject;

public record VanillaLootPool(float bonusRoll, KList<VanillaLootPoolEntry> entries, VanillaLootPoolRoll rolls) {
    public VanillaLootPool(KList<VanillaLootPoolEntry> entries) {
        this(0f, entries);
    }

    public VanillaLootPool() {
        this(0f, new KList<>());
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("bonus_rolls", this.bonusRoll());

        JSONArray entries = new JSONArray();
        for (VanillaLootPoolEntry entry : this.entries) {
            entries.put(entry.toJSONObject());
        }
        json.put("functions", entries);
        json.put("rolls", this.rolls());

        return json;
    }
}