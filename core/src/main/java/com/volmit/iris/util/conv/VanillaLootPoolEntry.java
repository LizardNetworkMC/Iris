package com.volmit.iris.util.conv;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.json.JSONArray;
import com.volmit.iris.util.json.JSONObject;

public record VanillaLootPoolEntry(String type, String name, int weight, KList<VanillaLootPoolFunction> functions) {
    public VanillaLootPoolEntry(String name, int weight, KList<VanillaLootPoolFunction> functions) {
        this("minecraft:item", name, weight, functions);
    }

    public VanillaLootPoolEntry(String type, String name) {
        this(type, name, 1, new KList<>());
    }

    public VanillaLootPoolEntry(String name) {
        this("minecraft:item", name, 1, new KList<>());
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("type", this.type());
        json.put("name", this.name());
        json.put("weight", this.weight());

        JSONArray funcs = new JSONArray();
        for (VanillaLootPoolFunction func : this.functions) {
            funcs.put(func.toJSONObject());
        }
        json.put("functions", funcs);

        return json;
    }
}
