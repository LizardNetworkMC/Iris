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
 *  - 2026-06-13 @xIRoXaSx: Removed Kotlin scripting system (security: packs must not execute arbitrary code).
 */

package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.*;
import com.volmit.iris.engine.object.annotations.functions.ResourceLoadersFunction;
import com.volmit.iris.util.collection.KList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Desc("Represents global preprocessors")
public class IrisPreProcessors {
    @Required
    @Desc("The preprocessor type")
    @RegistryListFunction(ResourceLoadersFunction.class)
    private String type = "dimension";

    @Required
    @Desc("The preprocessor scripts (scripting system removed)")
    @ArrayType(type = String.class, min = 1)
    private KList<String> scripts = new KList<>();
}
