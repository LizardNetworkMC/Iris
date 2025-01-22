package com.volmit.iris.util.conv;

import com.volmit.iris.util.json.JSONObject;

public record VanillaLootPoolFunctionCount(String type, float max, float min) {
    public VanillaLootPoolFunctionCount(float max, float min) {
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
