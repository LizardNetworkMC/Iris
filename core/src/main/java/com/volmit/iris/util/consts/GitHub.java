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
 *  - 2025-01-30 @xIRoXaSx: Updated dimension pack tag.
 */

package com.volmit.iris.util.consts;

public class GitHub {
    private static final String dimensionPackName = "overworld";
    private static final String dimensionPackOrganization = "LizardNetworkMC";
    private static final String dimensionPackTag = "v3.1.1";

    public static String getDimensionPackName() {
        return dimensionPackName;
    }

    public static String getDimensionPackOrganization() {
        return dimensionPackOrganization;
    }

    public static String getDimensionPackTag() {
        return dimensionPackTag;
    }

    public static String getDimensionPackRepo() {
        return String.format("%s/%s", dimensionPackOrganization, dimensionPackName);
    }

    public static String getDimensionPackBaseUrl() {
        return String.format("https://github.com/%s/%s", dimensionPackOrganization, dimensionPackName);
    }

    public static String getDimensionPackArchiveUrl() {
        return String.format("%s/archive/refs/tags/%s.zip", getDimensionPackBaseUrl(), dimensionPackTag);
    }
}
