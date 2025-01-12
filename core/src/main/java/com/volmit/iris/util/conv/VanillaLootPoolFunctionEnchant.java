package com.volmit.iris.util.conv;

public record VanillaLootPoolFunctionEnchant(String function, String options) {
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("function", this.function());
        json.put("options", this.options());

        return json;
    }
}