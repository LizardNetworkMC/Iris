/*
 * Iris is a World Generator for Minecraft Bukkit Servers
 * Copyright (c) 2022 Arcane Arts (Volmit Software)
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
 *  - 2026-06-13 @xIRoXaSx: Removed orphaned script-execution method left over from Kotlin scripting removal.
 */

package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.math.RNG;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;

import java.io.File;

@Data
@EqualsAndHashCode(callSuper = false)
public class IrisVanillaLootTable extends IrisLootTable {
    private final LootTable lootTable;

    @Override
    public String getName() {
        return "Vanilla " + lootTable.getKey();
    }

    @Override
    public int getRarity() {
        return 0;
    }

    @Override
    public int getMaxPicked() {
        return 0;
    }

    @Override
    public int getMinPicked() {
        return 0;
    }

    @Override
    public int getMaxTries() {
        return 0;
    }

    @Override
    public KList<IrisLoot> getLoot() {
        return new KList<>();
    }

    @Override
    public KList<ItemStack> getLoot(boolean debug, RNG rng, InventorySlotType slot, World world, int x, int y, int z) {
        return new KList<>(lootTable.populateLoot(rng, new LootContext.Builder(new Location(world, x, y, z)).build()));
    }

    @Override
    public String getFolderName() {
        throw new UnsupportedOperationException("VanillaLootTables do not have a folder name");
    }

    @Override
    public String getTypeName() {
        throw new UnsupportedOperationException("VanillaLootTables do not have a type name");
    }

    @Override
    public File getLoadFile() {
        throw new UnsupportedOperationException("VanillaLootTables do not have a load file");
    }

    @Override
    public IrisData getLoader() {
        throw new UnsupportedOperationException("VanillaLootTables do not have a loader");
    }
}
