package com.volmit.iris.util.conv;

public record VanillaLootPoolFunctionDamage(float max, float min) {
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("max", this.max());
        json.put("min", this.min());

        return json;
    }
}