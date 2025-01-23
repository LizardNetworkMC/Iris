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

import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.conv.VanillaLootPoolEntry;
import com.volmit.iris.util.json.JSONArray;
import com.volmit.iris.util.json.JSONObject;

public record VanillaLootPool(float bonusRoll, KList<VanillaLootPoolEntry> entries, VanillaLootPoolRoll roll) {
    public VanillaLootPool(KList<VanillaLootPoolEntry> entries, VanillaLootPoolRoll roll) {
        this(0f, entries, roll);
    }

    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("bonus_rolls", this.bonusRoll());

        JSONArray entries = new JSONArray();
        for (VanillaLootPoolEntry entry : this.entries) {
            entries.put(entry.toJSONObject());
        }
        json.put("entries", entries);
        json.put("rolls", this.roll().toJSONObject());

        return json;
    }
}
