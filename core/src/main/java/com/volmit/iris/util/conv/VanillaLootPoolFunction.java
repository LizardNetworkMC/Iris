package com.volmit.iris.util.conv;

import com.google.code.gson.Gson;
import com.volmit.iris.util.conv.VanillaLootFunctionCount;
import com.volmit.iris.util.json.JSONObject;

public class VanillaLootPoolFunction {
    private final boolean add;
    
    private String function = "";
    private VanillaLootPoolFunctionCount count;
    private VanillaLootPoolFunctionDamage damage;
    private VanillaLootPoolFunctionEnchant enchant;

    public VanillaLootPoolFunction(String function, boolean add) {
        this.function = function;
        this.add = add;
    }
    
    public VanillaLootPoolFunction(String function) {
        this.function = function;
        this.add = false;
    }

    public VanillaLootPoolFunction() {
        this.add = false;
    }

    public VanillaLootPoolFunction setCount(VanillaLootPoolFunctionCount count) {
        this.function = "set_count";
        this.count = count;
        return this;
    }

    public VanillaLootPoolFunction setDamage(VanillaLootPoolFunctionDamage damage) {
        this.function = "set_damage";
        this.damage = damage;
        return this;
    }

    public VanillaLootPoolFunction setEnchant(VanillaLootPoolFunctionEnchant enchant) {
        this.enchant = enchant;
        return this;
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("add", this.add);
        json.put("function", this.function);

        if (this.count != null) {
            json.put("count", this.count);
        } else if (this.damage != null) {
            json.put("damage", this.damage);
        } else if (this.enchant != null) {
            json.put("function", this.enchant.function());
            json.put("options", this.enchant.options());
        }

        return json;
    }
}