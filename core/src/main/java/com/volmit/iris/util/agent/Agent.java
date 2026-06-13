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
 *  - 2026-06-13 @xIRoXaSx: Added documentation clarifying agent purpose (NMS hooks, not scripting).
 */

package com.volmit.iris.util.agent;

import com.volmit.iris.Iris;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Provides JVM Instrumentation for Iris's NMS hooks.
 *
 * At plugin startup, Iris uses ByteBuddy to redefine Minecraft's ServerLevel,
 * ChunkAccess and ProtoChunk classes so it can intercept chunk generation calls
 * that the Paper/Spigot API does not otherwise expose. The agent JAR is bundled
 * inside the plugin resources (not downloaded from the network) and is attached
 * once per JVM process via {@link #install()}.
 */
public class Agent {
    private static final String NAME = "com.volmit.iris.util.agent.Installer";
    public static final File AGENT_JAR = new File(Iris.instance.getDataFolder(), "agent.jar");

    public static ClassReloadingStrategy installed() {
        return ClassReloadingStrategy.of(getInstrumentation());
    }

    public static Instrumentation getInstrumentation() {
        Instrumentation instrumentation = doGetInstrumentation();
        if (instrumentation == null) throw new IllegalStateException("The agent is not initialized or unavailable");
        return instrumentation;
    }

    public static boolean install() {
        if (doGetInstrumentation() != null)
            return true;
        try {
            Files.copy(Iris.instance.getResource("agent.jar"), AGENT_JAR.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Iris.info("Installing Java Agent...");
            ByteBuddyAgent.attach(AGENT_JAR, ByteBuddyAgent.ProcessProvider.ForCurrentVm.INSTANCE);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return doGetInstrumentation() != null;
    }

    private static Instrumentation doGetInstrumentation() {
        try {
            return (Instrumentation) Class.forName(NAME, true, ClassLoader.getSystemClassLoader()).getMethod("getInstrumentation").invoke(null);
        } catch (Exception ex) {
            return null;
        }
    }
}
