package com.volmit.iris.util.conv;

import com.volmit.iris.engine.object.IrisEnchantment;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.json.JSONArray;
import com.volmit.iris.util.json.JSONObject;

public record VanillaLootPoolFunctionEnchant(String function, String options, KList<IrisEnchantment> enchantment) {
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("function", this.function());
        json.put("options", this.options());

        JSONArray enchants = new JSONArray();
        for (IrisEnchantment enchantment : this.enchantment) {
            enchants.put(enchantment.getEnchantment());
        }
        json.put("enchantments", enchants);

        return json;
    }
}
