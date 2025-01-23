/*
 * Iris is a World Generator for Minecraft Bukkit Servers
 * Copyright (c) 2025 xIRoXaSx
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Changes (YYYY-MM-DD):
 *  - 2025-01-23 @xIRoXaSx: Added file.
 */

package com.volmit.iris.util.conv;

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
        this.function = "minecraft:set_count";
        this.count = count;
        return this;
    }

    public VanillaLootPoolFunction setDamage(VanillaLootPoolFunctionDamage damage) {
        this.function = "minecraft:set_damage";
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
            json.put("count", this.count.toJSONObject());
        } else if (this.damage != null) {
            json.put("damage", this.damage.toJSONObject());
        } else if (this.enchant != null) {
            json.put("function", this.enchant.function());
            json.put("options", this.enchant.options());
        }

        return json;
    }
}
