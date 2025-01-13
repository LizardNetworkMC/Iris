package com.volmit.iris.util.conv;

import com.volmit.iris.util.json.JSONObject;

public record VanillaLootPoolFunctionDamage(String type, double max, double min) {
    public VanillaLootPoolFunctionDamage(double max, double min) {
        this("minecraft:uniform", max, min);
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("type", this.type());
        json.put("max", this.max());
        json.put("min", this.min());

        return json;
    }
}