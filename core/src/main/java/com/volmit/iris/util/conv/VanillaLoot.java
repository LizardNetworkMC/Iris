package com.volmit.iris.util.conv;

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.conv.VanillaLootPool;

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

        return json.toString();
    }
}